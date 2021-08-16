package com.hxs.ktutil.core.media


import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

//    val yyMMDDFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
//    val yymmddFormat = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
//    val mmDDFormat = SimpleDateFormat("MM月dd日", Locale.CHINA)
//    val yyMMFormat = SimpleDateFormat("yyyy年MM月", Locale.CHINA)
//    val yyFormat = SimpleDateFormat("yyyy年", Locale.CHINA)

//    private val _yyMMFormat = SimpleDateFormat("yyy-MM", Locale.CHINA)
//    private val _yyFormat = SimpleDateFormat("yyy", Locale.CHINA)

    private val calendar = Calendar.getInstance()

    private fun setDayOfStart(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    /**
     * 获取选择天的开始和结束时间
     */
    fun requestDayRange(time: Long): Pair<Long, Long> {
        calendar.timeInMillis = time
        setDayOfStart(calendar)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endTime = calendar.timeInMillis
        return Pair(startTime, endTime)
    }

    /**
     * 获取选定时间的上一月份
     */
    fun lastMonth(time: Long): Long {
        calendar.timeInMillis = time
        calendar.add(Calendar.MONTH, -1)
        return calendar.timeInMillis
    }


    /**
     * 获取选定时间的下一月份
     */
    fun nextMonth(time: Long): Long {
        calendar.timeInMillis = time
        calendar.add(Calendar.MONTH, 1)
        return calendar.timeInMillis
    }

    /**
     * 获取选择月的开始和结束时间
     */
    fun requestMonthRange(time: Long): Pair<Long, Long> {
        calendar.timeInMillis = time
        setDayOfStart(calendar)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endTime = calendar.timeInMillis
        return Pair(startTime, endTime)
    }


    fun lastYear(time: Long): Long {
        calendar.timeInMillis = time
        calendar.add(Calendar.YEAR, -1)
        return calendar.timeInMillis
    }


    fun nextYear(time: Long): Long {
        calendar.timeInMillis = time
        calendar.add(Calendar.YEAR, 1)
        return calendar.timeInMillis
    }


    /**
     * 获取选择年的开始和结束时间
     */
    fun requestYearRange(time: Long): Pair<Long, Long> {
        calendar.timeInMillis = time
        setDayOfStart(calendar)
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.YEAR, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endTime = calendar.timeInMillis
        return Pair(startTime, endTime)
    }


//    /**
//     * 设置默认值，即选取当前时间取当前月份
//     */
//    fun defaultChartDate(): ChartDate {
//        val pair = requestMonthRange(System.currentTimeMillis())
//        return ChartDate(System.currentTimeMillis(), pair.first, pair.second, yyMMFormat)
//    }


//    fun time2YYMMDD(timestamp: Long = System.currentTimeMillis()): String {
//        return yyMMDDFormat.format(Date(timestamp))
//    }
//
//    fun time2YYMM(timestamp: Long = System.currentTimeMillis()): String {
//        return yyMMFormat.format(Date(timestamp))
//    }
//
//    fun time2MMDD(timestamp: Long = System.currentTimeMillis()): String {
//        return mmDDFormat.format(Date(timestamp))
//    }

    fun getYear(time: Long = System.currentTimeMillis()): Int {
        calendar.timeInMillis = time
        return calendar.get(Calendar.YEAR)
    }

    fun get(type: Int, time: Long = System.currentTimeMillis()): Int {
        calendar.timeInMillis = time
        return calendar.get(type)
    }

    fun set(time: Long = System.currentTimeMillis(), type: Int, value: Int): Long {
        calendar.timeInMillis = time
        calendar.set(type, value)
        return calendar.timeInMillis
    }

    /**
     * 判断是否是最新的一天
     */
    fun isNewestDay(time: Long): Boolean {
        val curYear = get(Calendar.YEAR)
        val curDate = get(Calendar.DAY_OF_YEAR)
        val selectedYear = get(Calendar.YEAR, time)
        val selectedDate = get(Calendar.DAY_OF_YEAR, time)
        return (selectedYear - curYear) * 365 + selectedDate - curDate >= 0

    }

    /**
     * 判断是否是最新的一个月
     */
    fun isNewestMonth(time: Long): Boolean {
        val curYear = get(Calendar.YEAR)
        val curMonth = get(Calendar.MONTH)
        val selectedYear = get(Calendar.YEAR, time)
        val selectedMonth = get(Calendar.MONTH, time)
        return (selectedYear - curYear) * 12 + selectedMonth - curMonth >= 0

    }


    /**
     * 判断是否是最新的一年
     */
    fun isNewestYear(time: Long): Boolean {
        val curYear = get(Calendar.YEAR)
        val selectedYear = get(Calendar.YEAR, time)
        return selectedYear >= curYear
    }


    /**
     * 根据年月日确定时间戳
     *
     * param [normalize] 是否标准化，将时分秒设为0
     */
    fun createYMDTimeStamp(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        normalize: Boolean = false
    ): Long {
        if (normalize) {
            setDayOfStart(calendar)
        }
        calendar.set(year, month, dayOfMonth)
        return calendar.timeInMillis
    }


    /**
     * 传入时间格式和时间戳获得格式化字符串
     */
    fun time2Format(
        format: SimpleDateFormat,
        timestamp: Long = System.currentTimeMillis()
    ): String {
        return format.format(Date(timestamp))
    }

}