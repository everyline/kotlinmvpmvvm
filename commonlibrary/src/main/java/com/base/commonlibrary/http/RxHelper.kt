package com.base.commonlibrary.http

import com.base.commonlibrary.base.BaseResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/27
 *描述:TODO
 */
object RxHelper {


    fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer<T, T> { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> handleMyResult(): FlowableTransformer<BaseResponse<T?>?, T?> {
        return FlowableTransformer {
            return@FlowableTransformer it.onErrorResumeNext(ErrorResumeFunction()).flatMap {
                var code: Int = it!!.code
                var msg: String? = it?.msg
                return@flatMap if (code === 1) {
                    createData(it?.data)
                } else {
                    Flowable.error(ApiException(code, msg))
                }

            }
        }
    }

    fun <T> createData(t: T): Flowable<T> {
        return Flowable.create({ emitter ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }, BackpressureStrategy.BUFFER)
    }


    class ErrorResumeFunction<T> : Function<Throwable, Flowable<out BaseResponse<T>>> {
        override fun apply(t: Throwable): Flowable<out BaseResponse<T>> {
            return Flowable.error(ExceptionHandle.handleException(t))
        }
    }


}


