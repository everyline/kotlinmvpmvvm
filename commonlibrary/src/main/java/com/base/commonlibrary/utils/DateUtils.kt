package com.base.commonlibrary.utils

import com.blankj.utilcode.util.LogUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * created by 天天挂线
 * email:746608431@qq.com
 * on 2020/5/5
 */
object DateUtils {
    //获取明天
    fun getTomorrow(date: Date?): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    //获取昨天
    fun getYesterday(date: Date?): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    //获取明天
    val tomorrow: String
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            return format.format(calendar.time)
        }

    //获取昨天
    val yesterday: String
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            return format.format(calendar.time)
        }

    //判断2个时间大小
    fun compare_date(date1: String?, date2: String?): Int {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            val dt1 = df.parse(date1)
            val dt2 = df.parse(date2)
            return if (dt1.time > dt2.time) {
                LogUtils.d("dateutils==", "dt1 比dt2大")
                1
            } else if (dt1.time < dt2.time) {
                LogUtils.d("dateutils==", "dt1比dt2小")
                -1
            } else {
                LogUtils.d("dateutils==", "dt1和dt2相等")
                0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return 0
    }
}