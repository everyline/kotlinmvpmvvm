package com.base.commonlibrary.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.base.commonlibrary.basemvp.MVPIBaseView
import com.base.commonlibrary.basemvp.MVPIPresenter
import com.base.commonlibrary.view.MultipleStatusView
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.jaeger.library.StatusBarUtil
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/28
 *描述:TODO
 */
abstract class BaseDialogMVPFragment<T : MVPIPresenter<*>> : DialogFragment(), MVPIBaseView {

    private var isViewPrepare = false //是否可见状态
    private var isPrepared = false //标志位，View已经初始化完成。
    private var isFirstLoad = true //是否第一次加载

    protected var mLayoutStatusView: MultipleStatusView? = null
    protected val mRxPermissions: RxPermissions by lazy { RxPermissions(this) }
    protected var mContext: Context? = null
    protected var mActivity: Activity? = null
    private var mPresenter: T? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        this.mActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirstLoad = true
        isPrepared = true
        StatusBarUtil.setColor(mActivity, Color.WHITE, 0)
        initView()
        getTitleBar()
        lazyLoad()
        initListener()
        initRetryClickListener()
        if (getTitleBar() != 0) {
            val titleBar = view.findViewById<TitleBar>(getTitleBar())
            titleBar.setOnTitleBarListener(object : OnTitleBarListener {
                override fun onLeftClick(v: View?) {

                }

                override fun onRightClick(v: View?) {
                    onRightClickListener(v)
                }

                override fun onTitleClick(v: View?) {

                }

            })
        }


    }

    //和viewapger 配合使用
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isViewPrepare = true
            onVisible()
        } else {
            isViewPrepare = false
            onInvisible()
        }
    }

    fun onInvisible() {

    }

    fun onVisible() {

    }

    //如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            isViewPrepare = true
            onVisible()
        } else {
            isViewPrepare = false
            onInvisible()
        }
    }


    protected abstract fun getTitleBar(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()
    protected abstract fun getLayoutId(): Int

    protected abstract fun onRightClickListener(v: View?)

    protected open fun lazyLoad() {
        if (!isPrepared || !userVisibleHint || !isFirstLoad) {
            return
        }
        isFirstLoad = false
        initData()
    }

    fun initRetryClickListener() {

        mLayoutStatusView?.setOnRetryClickListener(RetryClickListener())
    }


    inner class RetryClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            lazyLoad()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}