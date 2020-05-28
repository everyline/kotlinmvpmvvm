package com.example.myapplication

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import kotlin.properties.Delegates

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/28
 *描述:TODO
 */
class APP : MultiDexApplication() {
    companion object {
        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext;
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}