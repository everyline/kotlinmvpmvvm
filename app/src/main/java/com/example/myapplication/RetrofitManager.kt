package com.example.myapplication

import com.blankj.utilcode.util.AppUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/28
 *描述:TODO
 */
object RetrofitManager {

    val apiService: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit().create(apiService::class.java)
    }


    //设置公共参数
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url.newBuilder()
                .addQueryParameter("deviceModel", AppUtils.getAppVersionName())
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)

        }
    }

    //设置头
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("token", "11")
                .method(originalRequest.method, originalRequest.body)
            val request = requestBuilder.build()
            chain.proceed(request)

        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(httpLoggingInterceptor)
        }

        return httpClient.addInterceptor(addHeaderInterceptor())
            .addInterceptor(addQueryParameterInterceptor())
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }
}