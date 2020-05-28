package com.base.commonlibrary.http

import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/27
 *描述:TODO
 */
class RetryWhenDelay : Function<Flowable<Throwable>, Publisher<*>> {
    private val TAG: String = RetryWhenDelay::javaClass.name
    private val maxRetries = 3
    private val retryDelayMillis = 3000L
    private var retryCount = 0
    override fun apply(t: Flowable<Throwable>): Publisher<*> {
        return t.flatMap {
            return@flatMap if (it is ApiException) {
                LogUtils.d(TAG, "===进入重试机制===${retryCount}次")
                //todo 重试机制代码


                if (++retryCount <= maxRetries) {
                    Flowable.timer(retryDelayMillis, TimeUnit.MILLISECONDS)
                } else {
                    Flowable.error(it)
                }
            } else null
        }

    }

}


