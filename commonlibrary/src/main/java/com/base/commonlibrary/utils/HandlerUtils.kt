package com.base.commonlibrary.utils

import android.os.Handler
import android.os.Looper

/**
 * 文件描述：
 * 作者：G
 * 创建时间：2019/9/2
 */
object HandlerUtils {
    private val HANDLER = Handler(Looper.getMainLooper())
    fun runOnUiThread(runnable: Runnable?) {
        HANDLER.post(runnable)
    }

    fun runOnUiThreadDelay(runnable: Runnable?, delayMillis: Long) {
        HANDLER.postDelayed(runnable, delayMillis)
    }

    fun removeRunable(runnable: Runnable?) {
        HANDLER.removeCallbacks(runnable)
    }
}