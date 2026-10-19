package com.example.liuyao.liuren

fun buildAIPrompt(analysis: AnalysisResult, userQuestion: String): String {
    val chuan = analysis.sanChuan
    return buildString {
        appendLine("【系统指令】你是一位精通小六壬与五行生克的实战派命理师。以下数据由后台精确计算得出，请严格据此推理，不得编造。")
        appendLine()
        appendLine("# 后台卦象数据：")
        appendLine("- 初传（事始）：${chuan["初传"]?.name}（${chuan["初传"]?.wuxing}，${chuan["初传"]?.shen}）")
        appendLine("- 中传（事中）：${chuan["中传"]?.name}（${chuan["中传"]?.wuxing}，${chuan["中传"]?.shen}）")
        appendLine("- 末传（事终）：${chuan["末传"]?.name}（${chuan["末传"]?.wuxing}，${chuan["末传"]?.shen}）")
        appendLine("- 最终落宫：${analysis.finalGua}")
        appendLine("- 五行旺衰：${analysis.wangShuai}")
        appendLine("- 三传生克：${analysis.shengKe}")
        appendLine("- 识别场景：${analysis.scene}")
        appendLine("- 取象方位建议：${analysis.orientation}")
        appendLine("- 应期推荐：${analysis.yingQi.joinToString("、")}")
        appendLine()
        appendLine("# 用户具体问题：")
        appendLine(userQuestion)
        appendLine()
        appendLine("# 强制推理步骤（必须逐条执行）：")
        appendLine("1. 先分析三传生克是“顺生”还是“逆克”，据此判断事情发展过程是否顺利。")
        appendLine("2. 结合“五行旺衰”修正吉凶：若落宫虽为吉，但处“囚/死”地，则只说“有转机”而非“必定大吉”。")
        appendLine("3. 根据“识别场景”调用“取象方位”给出具体、可操作的现实建议（如失物具体去哪找，感情如何处理）。")
        appendLine("4. 必须引用“应期推荐”中的日期，告知用户最佳行动时间窗口。")
        appendLine("5. 结尾必须给出一条“避忌提醒”：根据当前卦象五行，建议用户近期避免与相冲五行相关的事物。")
        appendLine()
        appendLine("# 输出格式（严格按此分段，不要跑题）：")
        appendLine("【卦象生克洞察】（150字内）")
        appendLine("【针对“${userQuestion}”的专项解答】（200字内，紧扣问题）")
        appendLine("【行动应期与避忌】（80字内，包含具体日期和避讳）")
    }
}