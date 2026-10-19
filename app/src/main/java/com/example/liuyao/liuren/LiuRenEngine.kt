package com.example.liuyao.liuren

import java.util.Calendar
import java.util.concurrent.TimeUnit

// ---------- 基础数据 ----------
object LiuRenData {
    val guaList = listOf("大安", "留连", "速喜", "赤口", "小吉", "空亡")
    val wuxingMap = mapOf(
        "大安" to "木", "留连" to "水", "速喜" to "火",
        "赤口" to "金", "小吉" to "木", "空亡" to "土"
    )
    val guaShen = mapOf(
        "大安" to "青龙", "留连" to "玄武", "速喜" to "朱雀",
        "赤口" to "白虎", "小吉" to "六合", "空亡" to "勾陈"
    )
    val wuxingOrder = listOf("木", "火", "土", "金", "水")

    // 旺衰表（季节 -> 五行状态）
    val wangShuai = mapOf(
        "木" to mapOf("春" to "旺", "夏" to "休", "秋" to "囚", "冬" to "相"),
        "火" to mapOf("春" to "相", "夏" to "旺", "秋" to "休", "冬" to "囚"),
        "土" to mapOf("春" to "死", "夏" to "相", "秋" to "生", "冬" to "休"),
        "金" to mapOf("春" to "囚", "夏" to "死", "秋" to "旺", "冬" to "休"),
        "水" to mapOf("春" to "休", "夏" to "囚", "秋" to "死", "冬" to "旺")
    )

    // 场景关键词（自动识别用户意图）
    val sceneKeywords = mapOf(
        "失物" to listOf("丢", "找", "包", "钱包", "手机", "钥匙", "证件", "遗失", "被盗", "不见", "在哪"),
        "感情" to listOf("对象", "男朋友", "女朋友", "复合", "分手", "喜欢", "暗恋", "婚姻", "结婚", "出轨", "冷战"),
        "事业" to listOf("工作", "面试", "升职", "跳槽", "辞职", "领导", "老板", "生意", "合作", "项目", "offer"),
        "健康" to listOf("病", "疼", "痛", "头晕", "住院", "手术", "检查", "身体", "虚弱", "感冒"),
        "官司" to listOf("诉讼", "官司", "吵架", "纠纷", "仲裁", "比赛", "竞争", "敌对")
    )

    // 场景取象映射（方位/状态）
    val sceneMapping = mapOf(
        "失物" to mapOf(
            "木" to "草丛、木制家具、衣柜、书籍旁、西南/东方",
            "火" to "厨房、电器旁、红色衣物内、南方",
            "土" to "高处架子、固定大件家具下、墙角、东北方",
            "金" to "金属盒、保险柜、包包夹层、汽车内、西方",
            "水" to "厕所、水槽旁、鱼缸边、车内水杯架、北方"
        ),
        "感情" to mapOf(
            "木" to "善良但优柔，关系靠朋友长辈推动",
            "火" to "激情热烈但短暂，易闪婚闪离",
            "土" to "稳重固执，感情迟钝，需外力打破",
            "金" to "口舌争吵，价值观不合，异地易断",
            "水" to "暧昧不清，暗中纠缠，或有第三者"
        ),
        "事业" to mapOf(
            "木" to "竞争激烈，需靠人脉或文书取胜",
            "火" to "面试顺利，文书即达，但职位虚火",
            "土" to "稳定但升迁慢，适合国企地产",
            "金" to "领导施压，涉及科技/金属行业",
            "水" to "暗流涌动，适合策划公关，不宜冒进"
        ),
        "健康" to mapOf(
            "木" to "肝胆、神经、筋骨",
            "火" to "心脏、血液、眼睛、炎症",
            "土" to "脾胃、消化、浮肿、湿气",
            "金" to "肺、呼吸道、皮肤、牙齿",
            "水" to "肾、妇科、泌尿、耳"
        ),
        "官司" to mapOf(
            "木" to "调解为主，有第三方介入",
            "火" to "文书证据重要，但易激化",
            "土" to "拖延，反复，僵持不下",
            "金" to "对方强势，官非必现",
            "水" to "暗中操作，有转圜余地"
        )
    )

    // 应期地支映射（落宫 -> 对应地支）
    val yingqiDizhi = mapOf(
        "大安" to listOf("寅", "卯", "亥", "子"),
        "留连" to listOf("亥", "子", "申", "酉"),
        "速喜" to listOf("巳", "午", "寅", "卯"),
        "赤口" to listOf("申", "酉", "辰", "戌"),
        "小吉" to listOf("寅", "卯", "亥", "未"),
        "空亡" to listOf("辰", "戌", "丑", "未")
    )

    val dizhiList = listOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
}

// ---------- 五行生克函数 ----------
fun getWuxingRelation(w1: String, w2: String): String {
    if (w1 == w2) return "比和"
    val order = LiuRenData.wuxingOrder
    val idx1 = order.indexOf(w1)
    val idx2 = order.indexOf(w2)
    return when {
        (idx1 + 1) % 5 == idx2 -> "生"
        (idx1 - 1) % 5 == idx2 -> "被生"
        (idx1 + 2) % 5 == idx2 -> "克"
        (idx1 - 2) % 5 == idx2 -> "被克"
        else -> "比和"
    }
}

// ---------- 数据类 ----------
data class SanChuanItem(val name: String, val wuxing: String, val shen: String)
data class AnalysisResult(
    val sanChuan: Map<String, SanChuanItem>, // "初传", "中传", "末传"
    val finalGua: String,
    val wangShuai: String,      // 例如 "木处于春季，状态「旺」"
    val shengKe: String,        // 例如 "火克金，金被木克"
    val scene: String,          // "失物"/"感情"/...
    val orientation: String,    // 取象方位描述
    val yingQi: List<String>,   // 应期日期列表
    val grade: String           // 大吉/中吉/小吉/小凶/中凶/大凶
)

// ---------- 核心引擎 ----------
class LiuRenEngine(private val monthNum: Int, private val dayNum: Int, private val hourNum: Int) {

    // 输入取模（1~6 循环）
    private val m = monthNum.mod(6).let { if (it == 0) 6 else it }
    private val d = dayNum.mod(6).let { if (it == 0) 6 else it }
    private val h = hourNum.mod(6).let { if (it == 0) 6 else it }

    // 三传
    val first: String = LiuRenData.guaList[(m - 1) % 6]
    val middle: String = LiuRenData.guaList[(m + d - 2) % 6]
    val last: String = LiuRenData.guaList[(m + d + h - 3) % 6]
    val finalGua: String = last

    // 五行
    val firstWx = LiuRenData.wuxingMap[first]!!
    val middleWx = LiuRenData.wuxingMap[middle]!!
    val lastWx = LiuRenData.wuxingMap[last]!!
    val finalWx = LiuRenData.wuxingMap[finalGua]!!

    // 生克
    val rel12 = getWuxingRelation(firstWx, middleWx)
    val rel23 = getWuxingRelation(middleWx, lastWx)

    /**
     * 获取完整分析数据（供AI使用）
     * @param userQuestion 用户输入的具体问题（用于场景识别）
     */
    fun getAnalysis(userQuestion: String = ""): AnalysisResult {
        // 1. 场景识别
        var scene = "通用"
        for ((key, words) in LiuRenData.sceneKeywords) {
            if (words.any { userQuestion.contains(it) }) {
                scene = key
                break
            }
        }

        // 2. 旺衰（基于当前季节）
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1 // 1~12
        val season = when (month) {
            in 3..5 -> "春"
            in 6..8 -> "夏"
            in 9..11 -> "秋"
            else -> "冬"
        }
        val wangStatus = LiuRenData.wangShuai[finalWx]?.get(season) ?: "平"

        // 3. 应期推算（最近3个地支日）
        val targetDizhi = LiuRenData.yingqiDizhi[finalGua] ?: emptyList()
        val yingQiDates = mutableListOf<String>()
        // 基准日（实际项目建议接入农历库，这里用公历模拟，你可替换为自己的日期计算）
        // 注意：若你的App有农历转换，请替换此逻辑为真实农历日柱
        for (i in 1..30) {
            val future = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, i) }
            val dizhi = getDizhiByDate(future)
            if (dizhi in targetDizhi) {
                yingQiDates.add(String.format("%d月%d日", future.get(Calendar.MONTH) + 1, future.get(Calendar.DAY_OF_MONTH)))
                if (yingQiDates.size >= 3) break
            }
        }

        // 4. 取象方位
        val orientation = LiuRenData.sceneMapping[scene]?.get(finalWx) ?: "无特定方位"

        // 5. 吉凶等级
        val grade = when (finalGua) {
            "小吉" -> "大吉"
            "速喜" -> "中吉"
            "大安" -> "小吉"
            "留连" -> "小凶"
            "赤口" -> "中凶"
            else -> "大凶"
        }

        return AnalysisResult(
            sanChuan = mapOf(
                "初传" to SanChuanItem(first, firstWx, LiuRenData.guaShen[first]!!),
                "中传" to SanChuanItem(middle, middleWx, LiuRenData.guaShen[middle]!!),
                "末传" to SanChuanItem(last, lastWx, LiuRenData.guaShen[last]!!)
            ),
            finalGua = finalGua,
            wangShuai = "${finalWx}处于${season}季，状态「${wangStatus}」",
            shengKe = "${firstWx}${rel12}${middleWx}，${middleWx}${rel23}${lastWx}",
            scene = scene,
            orientation = orientation,
            yingQi = yingQiDates.ifEmpty { listOf("近期无特定应期") },
            grade = grade
        )
    }

    // ---------- 地支计算辅助（可替换为真实农历库） ----------
    private fun getDizhiByDate(date: Calendar): String {
        // 用公历日期的天数差值模拟（实际项目请接入农历转换库）
        // 这里以 2026-09-03 为基准，对应地支 "申"
        val baseCal = Calendar.getInstance().apply {
            set(2026, 8, 3) // 注意月份从0开始，8=9月
        }
        val diffMillis = date.timeInMillis - baseCal.timeInMillis
        val diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS)
        val idx = (diffDays % 12 + 12) % 12  // 确保正数
        return LiuRenData.dizhiList[idx.toInt()]
    }
}