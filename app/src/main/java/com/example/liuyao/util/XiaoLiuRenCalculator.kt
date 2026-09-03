package com.example.liuyao.util

/**
 * 小六壬计算工具类
 * 六神：大安、留连、速喜、赤口、小吉、空亡
 */
object XiaoLiuRenCalculator {

    // 六神名称
    private val liuShenName = arrayOf("大安", "留连", "速喜", "赤口", "小吉", "空亡")

    // 六神属性（吉/凶）
    private val liuShenAttribute = arrayOf("吉", "凶", "吉", "凶", "吉", "凶")

    // 六神断语
    private val liuShenDuanYu = arrayOf(
        // 大安
        "大安事事昌，求财在坤方，失物去不远，宅舍保安康，行人身未动，病者主无妨，将军回田野，仔细好推详。",
        // 留连
        "留连事难成，求谋日未明，官事凡宜缓，去者未回程，失物南方见，急讨方心称，更须防口舌，人口且平平。",
        // 速喜
        "速喜喜来临，求财向南行，失物申未午，逢人路上寻，官事有福德，病者无祸侵，田宅六畜吉，行人有信音。",
        // 赤口
        "赤口主口舌，官非切宜防，失物速速讨，行人有惊慌，六畜多作怪，病者出西方，更须防咒咀，诚恐染瘟殃。",
        // 小吉
        "小吉最吉昌，路上好商量，阴人来报喜，失物在坤方，行人即便至，交关甚是强，凡事皆和合，病者叩穷苍。",
        // 空亡
        "空亡事不祥，阴人少主张，求财无利益，行人有灾殃，失物寻不见，官事有刑伤，病人逢暗鬼，解禳保安康。"
    )

    // 六神方位
    private val liuShenFangWei = arrayOf(
        "东方", "北方", "南方", "西方", "西南", "中央"
    )

    // 六神五行
    private val liuShenWuXing = arrayOf(
        "木", "水", "火", "金", "水", "土"
    )

    // 计算小六壬
    // 入参：农历月、日、时辰序号
    // 返回：月宫、日宫、时宫（0-5对应六神）
    fun calculate(lunarMonth: Int, lunarDay: Int, shiChen: Int): Result {
        // 月宫：从大安起，数农历月份
        val monthIndex = (lunarMonth - 1) % 6

        // 日宫：从月宫起，数农历日期
        val dayIndex = (monthIndex + lunarDay - 1) % 6

        // 时宫：从日宫起，数时辰
        val hourIndex = (dayIndex + shiChen) % 6

        return Result(
            monthIndex = monthIndex,
            dayIndex = dayIndex,
            hourIndex = hourIndex
        )
    }

    // 获取六神名称
    fun getName(index: Int): String = liuShenName[index]

    // 获取六神属性
    fun getAttribute(index: Int): String = liuShenAttribute[index]

    // 获取六神断语
    fun getDuanYu(index: Int): String = liuShenDuanYu[index]

    // 获取六神方位
    fun getFangWei(index: Int): String = liuShenFangWei[index]

    // 获取六神五行
    fun getWuXing(index: Int): String = liuShenWuXing[index]

    // 判断是否吉利
    fun isJi(index: Int): Boolean = liuShenAttribute[index] == "吉"

    // 计算结果数据类
    data class Result(
        val monthIndex: Int,  // 月宫（0-5）
        val dayIndex: Int,    // 日宫（0-5）
        val hourIndex: Int    // 时宫（0-5）
    ) {
        fun getMonthName(): String = getName(monthIndex)
        fun getDayName(): String = getName(dayIndex)
        fun getHourName(): String = getName(hourIndex)

        fun getMonthAttribute(): String = getAttribute(monthIndex)
        fun getDayAttribute(): String = getAttribute(dayIndex)
        fun getHourAttribute(): String = getAttribute(hourIndex)

        fun getMonthDuanYu(): String = getDuanYu(monthIndex)
        fun getDayDuanYu(): String = getDuanYu(dayIndex)
        fun getHourDuanYu(): String = getDuanYu(hourIndex)

        fun isOverallJi(): Boolean = isJi(hourIndex)
    }
}
