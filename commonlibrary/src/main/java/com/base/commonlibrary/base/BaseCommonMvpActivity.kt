package com.base.commonlibrary.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.base.commonlibrary.basemvp.MVPBasePresenter
import com.base.commonlibrary.basemvp.MVPIBaseView
import com.base.commonlibrary.basemvp.MVPIPresenter
import com.base.commonlibrary.view.MultipleStatusView
import com.blankj.utilcode.util.KeyboardUtils
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.jaeger.library.R
import com.jaeger.library.StatusBarUtil
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/28
 *描述:TODO
 */
abstract class BaseCommonMvpActivity<T : MVPIPresenter<*>> : AppCompatActivity(), MVPIBaseView {
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null
    protected val mRxPermissions: RxPermissions by lazy { RxPermissions(this) }
    protected var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())


        StatusBarUtil.setColor(this, Color.WHITE, 0)
        getTitleBar()
        initView(savedInstanceState)
        initData()
        initListener()
        initRetryClickListener()
        if (getTitleBar() != 0) {
            val titleBar = findViewById<TitleBar>(getTitleBar())
            titleBar.setOnTitleBarListener(object : OnTitleBarListener {
                override fun onLeftClick(v: View?) {
                    finish()
                }

                override fun onRightClick(v: View?) {
                    onRightClickListener(v)
                }

                override fun onTitleClick(v: View?) {

                }

            })

        }
    }

    /**
     *  加载布局
     */
    abstract fun setLayoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化titlebar
     */

    abstract fun getTitleBar(): Int
    abstract fun initListener()
    abstract fun retryStart()
    abstract fun onRightClickListener(view: View?)

    protected fun initRetryClickListener() {
        mLayoutStatusView?.setOnRetryClickListener(RetryClickListener())
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        KeyboardUtils.hideSoftInput(this)
    }

    inner class RetryClickListener : View.OnClickListener {
        override fun onClick(v: View?) {

            retryStart()
        }
    }

}