package com.example.liuyao.util

import java.util.Calendar

/**
 * 农历工具类 - 阳历与农历精确转换
 * 支持范围：1900年-2100年
 */
object LunarCalendar {

    // 天干
    private val tianGan = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")

    // 地支
    private val diZhi = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

    // 生肖
    private val shengXiao = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")

    // 农历月份名称
    private val lunarMonthName = arrayOf(
        "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"
    )

    // 农历日期名称
    private val lunarDayName = arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )

    // 时辰名称
    private val shiChenName = arrayOf(
        "子时", "丑时", "寅时", "卯时", "辰时", "巳时",
        "午时", "未时", "申时", "酉时", "戌时", "亥时"
    )

    // 农历数据表（1900-2100年）
    // 每个元素表示该年的农历信息：
    // 高4位：闰月月份（0表示没有闰月）
    // 低16位：12个月的大小月信息（1表示30天，0表示29天）
    // 如果有闰月，第13位表示闰月的大小
    private val lunarInfo = longArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,
        0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0,
        0x0a2e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4,
        0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0,
        0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160,
        0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a2d0, 0x0d150, 0x0f252,
        0x0d520
    )

    // 获取农历某年的总天数
    fun lunarYearDays(year: Int): Int {
        var sum = 348
        var i = 0x8000
        while (i >= 0x10) {
            if ((lunarInfo[year - 1900] and i.toLong()) != 0L) sum += 1
            i = i shr 1
        }
        return sum + leapDays(year)
    }

    // 获取农历某年闰月的天数
    fun leapDays(year: Int): Int {
        return if (leapMonth(year) != 0) {
            if ((lunarInfo[year - 1900] and 0x10000L) != 0L) 30 else 29
        } else 0
    }

    // 获取农历某年闰月月份
    fun leapMonth(year: Int): Int {
        return (lunarInfo[year - 1900] and 0xfL).toInt()
    }

    // 获取农历某年某月的天数
    fun monthDays(year: Int, month: Int): Int {
        return if ((lunarInfo[year - 1900] and (0x10000 shr month).toLong()) != 0L) 30 else 29
    }

    // 阳历转农历
    fun solarToLunar(year: Int, month: Int, day: Int): LunarDate {
        val baseDate = Calendar.getInstance().apply {
            set(1900, 0, 31, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetDate = Calendar.getInstance().apply {
            set(year, month - 1, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var offset = ((targetDate.timeInMillis - baseDate.timeInMillis) / 86400000L).toInt()

        var lunarYear = 1900
        var temp = 0
        for (i in 1900..2100) {
            temp = lunarYearDays(i)
            if (offset < temp) break
            offset -= temp
            lunarYear++
        }

        val leap = leapMonth(lunarYear)
        var isLeap = false

        var lunarMonth = 1
        var i = 1
        while (i <= 12) {
            temp = monthDays(lunarYear, i)
            if (offset < temp) break

            if (leap > 0 && i == leap + 1 && !isLeap) {
                isLeap = true
                temp = leapDays(lunarYear)
            } else {
                offset -= temp
                if (!isLeap || i != leap + 1) lunarMonth++
                i++
            }
        }

        if (offset == 0 && leap > 0 && lunarMonth == leap + 1) {
            if (isLeap) {
                isLeap = false
            } else {
                isLeap = true
                --lunarMonth
            }
        }

        if (offset == 0 && isLeap) {
            isLeap = false
        }

        val lunarDay = offset + 1

        return LunarDate(lunarYear, lunarMonth, lunarDay, isLeap)
    }

    // 获取干支年
    fun getGanZhiYear(lunarYear: Int): String {
        val yearGan = tianGan[(lunarYear - 4) % 10]
        val yearZhi = diZhi[(lunarYear - 4) % 12]
        return "$yearGan${yearZhi}年"
    }

    // 获取生肖
    fun getShengXiao(lunarYear: Int): String {
        return shengXiao[(lunarYear - 4) % 12]
    }

    // 获取农历月名称
    fun getLunarMonthName(month: Int, isLeap: Boolean): String {
        val prefix = if (isLeap) "闰" else ""
        return "$prefix${lunarMonthName[month - 1]}月"
    }

    // 获取农历日名称
    fun getLunarDayName(day: Int): String {
        return lunarDayName[day - 1]
    }

    // 根据小时获取时辰
    fun getShiChen(hour: Int): Int {
        return ((hour + 1) / 2) % 12
    }

    // 获取时辰名称
    fun getShiChenName(hour: Int): String {
        return shiChenName[getShiChen(hour)]
    }

    // 农历日期数据类
    data class LunarDate(
        val year: Int,
        val month: Int,
        val day: Int,
        val isLeap: Boolean
    ) {
        fun getGanZhiYear(): String = getGanZhiYear(year)
        fun getShengXiao(): String = getShengXiao(year)
        fun getMonthName(): String = getLunarMonthName(month, isLeap)
        fun getDayName(): String = getLunarDayName(day)
    }
}
