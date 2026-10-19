package com.example.liuyao

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.liuyao.databinding.ActivityAiJieguaBinding
import com.example.liuyao.liuren.AiClient
import com.example.liuyao.liuren.LiuRenEngine
import com.example.liuyao.liuren.buildAIPrompt

/**
 * AI 智能解卦页面
 * 上栏：输入要问的问题；下栏：展示解卦结果；解卦按钮位于下栏下方
 */
class AIJieGuaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MONTH = "extra_month"   // 农历月（1~12）
        const val EXTRA_DAY = "extra_day"       // 农历日（1~30）
        const val EXTRA_HOUR = "extra_hour"     // 时辰序数（子=1 …… 亥=12）
    }

    private lateinit var binding: ActivityAiJieguaBinding
    private var engine: LiuRenEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiJieguaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val month = intent.getIntExtra(EXTRA_MONTH, 0)
        val day = intent.getIntExtra(EXTRA_DAY, 0)
        val hour = intent.getIntExtra(EXTRA_HOUR, 0)
        if (month > 0 && day > 0 && hour > 0) {
            engine = LiuRenEngine(month, day, hour)
        }

        binding.btnJieGua.setOnClickListener { startJieGua() }
    }

    private fun startJieGua() {
        val question = binding.etQuestion.text.toString().trim()
        if (question.isEmpty()) {
            Toast.makeText(this, R.string.ai_error_empty_question, Toast.LENGTH_SHORT).show()
            return
        }
        val eng = engine
        if (eng == null) {
            Toast.makeText(this, R.string.ai_need_calculate_first, Toast.LENGTH_LONG).show()
            return
        }

        // 进入解卦中状态
        binding.btnJieGua.isEnabled = false
        binding.btnJieGua.text = getString(R.string.ai_btn_loading)

        // 用用户问题做场景分析并构建 AI 提示词
        val prompt = buildAIPrompt(eng.getAnalysis(question), question)

        AiClient.chat(
            systemPrompt = prompt,
            onSuccess = { content ->
                runOnUiThread {
                    binding.tvResult.setTextColor(getColor(R.color.ink_black))
                    binding.tvResult.text = content
                    resetJieGuaButton()
                }
            },
            onError = { msg ->
                runOnUiThread {
                    binding.tvResult.setTextColor(getColor(R.color.xiong_color))
                    binding.tvResult.text = getString(R.string.ai_error_failed, msg)
                    resetJieGuaButton()
                }
            }
        )
    }

    private fun resetJieGuaButton() {
        binding.btnJieGua.isEnabled = true
        binding.btnJieGua.text = getString(R.string.ai_btn_jiegua)
    }
}