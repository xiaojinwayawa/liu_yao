package com.example.liuyao.liuren

/**
 * AI 接口配置文件（写死在代码中，按需修改）
 *
 * 本客户端采用 OpenAI 兼容协议（/chat/completions），
 * 以下任一厂商均可直接替换使用：
 *
 * 智谱AI(免费):  https://open.bigmodel.cn/api/paas/v4/chat/completions  模型: glm-4-flash
 *             （glm-4-flash 永久免费不限 Token，注册 open.bigmodel.cn 即用）
 * Kimi(月之暗面): https://api.moonshot.cn/v1/chat/completions           模型: kimi-latest
 *             （也可用 moonshot-v1-8k / moonshot-v1-32k / moonshot-v1-128k）
 * 硅基流动:      https://api.siliconflow.cn/v1/chat/completions          模型: Qwen/Qwen3-8B
 * DeepSeek:      https://api.deepseek.com/chat/completions               模型: deepseek-chat
 * 通义千问:       https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions  模型: qwen-plus
 * OpenAI:        https://api.openai.com/v1/chat/completions              模型: gpt-4o-mini
 */
object AiConfig {

    // 接口地址（默认智谱 AI 免费模型，可换成上面任意一家）
    const val BASE_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions"

    // API Key（注册 open.bigmodel.cn 后，在「API Keys」页面创建并填到这里）
    const val API_KEY = "b85095d08e7f43ae9ddbdd5b7f7b6ced.XxGR0wUzuMl67DNO"

    // 模型名称（与接口地址对应）
    const val MODEL = "glm-4-flash"

    // 单次请求超时（毫秒）
    const val CONNECT_TIMEOUT = 15_000
    const val READ_TIMEOUT = 90_000
}