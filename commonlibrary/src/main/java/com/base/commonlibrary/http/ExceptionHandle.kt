package com.base.commonlibrary.http

import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/27
 *描述:TODO
 */
class ExceptionHandle {

    companion object {
        //未知错误
        const val UNKNOWN = 1000

        //解析错误
        const val PARSE_ERROR = 1001

        //网络错误
        const val NETWORK_ERROR = 1002

        //协议错误
        const val HTTP_ERROR = 1003

        //参数错误
        const val SERVER_ERROR = 1004

        fun handleException(throwable: Throwable): ApiException {
            LogUtils.d(throwable)
            return when (throwable) {
                is SocketTimeoutException,
                is UnknownHostException,
                is ConnectException -> ApiException(NETWORK_ERROR, throwable.message)

                is JsonParseException,
                is JSONException,
                is ParseException -> ApiException(PARSE_ERROR, throwable.message)

                is IllegalArgumentException -> ApiException(SERVER_ERROR, throwable.message)

                is HttpException -> ApiException(HTTP_ERROR, throwable.message)

                else -> ApiException(UNKNOWN, throwable.message)
            }
        }

    }
}