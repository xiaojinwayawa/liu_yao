package com.example.liuyao.liuren

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * AI 接口调用客户端（OpenAI 兼容协议）
 * 使用 HttpURLConnection 实现，无第三方依赖
 */
object AiClient {

    /**
     * 发起一次对话请求
     * @param systemPrompt 完整系统提示词（由 AIPromptBuilder 构建）
     * @param onSuccess    成功回调（后台线程调用）
     * @param onError      失败回调（后台线程调用）
     */
    fun chat(systemPrompt: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        Thread {
            var conn: HttpURLConnection? = null
            try {
                if (AiConfig.API_KEY.isBlank()) {
                    onError("尚未配置 API Key，请在 AiConfig.kt 中填写")
                    return@Thread
                }

                conn = URL(AiConfig.BASE_URL).openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = AiConfig.CONNECT_TIMEOUT
                conn.readTimeout = AiConfig.READ_TIMEOUT
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Authorization", "Bearer ${AiConfig.API_KEY}")

                val body = JSONObject().apply {
                    put("model", AiConfig.MODEL)
                    put("messages", JSONArray().put(JSONObject().apply {
                        // 统一用 user 角色承载指令：智谱兼容接口对 system+字符串 content 会报 1214，
                        // user+字符串 在智谱/Kimi/DeepSeek/OpenAI 下均可用
                        put("role", "user")
                        put("content", systemPrompt)
                    }))
                    put("stream", false)
                    put("temperature", 0.7)
                }
                conn.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }

                val code = conn.responseCode
                val stream = if (code in 200..299) conn.inputStream else conn.errorStream
                val text = stream?.bufferedReader()?.use { it.readText() }.orEmpty()

                if (code !in 200..299) {
                    onError("HTTP $code: $text")
                    return@Thread
                }

                val content = JSONObject(text)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .optString("content")
                if (content.isBlank()) {
                    onError("接口返回内容为空")
                } else {
                    onSuccess(content)
                }
            } catch (e: Exception) {
                onError(e.message ?: "网络请求失败")
            } finally {
                conn?.disconnect()
            }
        }.start()
    }
}