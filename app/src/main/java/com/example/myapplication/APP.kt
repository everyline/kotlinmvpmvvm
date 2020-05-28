package com.example.myapplication

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/28
 *描述:TODO
 */
class APP : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}