package com.base.commonlibrary.utils

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * 展示转圈用的dialog
 * created by 天天挂线
 * email:746608431@qq.com
 * on 2020/5/5
 */
class DialogUtils private constructor() {
    private var tipDialog: QMUITipDialog? = null

    // 显示加载框
    fun showLoading(context: Context?) {
        dismissDialog()
        tipDialog = QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("请稍等")
            .create()
        tipDialog?.apply {
            window?.setGravity(Gravity.CENTER)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }

    }

    // 显示加载框
    fun showLoading(context: Context?, tips: String?) {
        dismissDialog()
        tipDialog = QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(if (TextUtils.isEmpty(tips)) "请稍等" else tips)
            .create()
        tipDialog?.apply {
            window?.setGravity(Gravity.CENTER)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    // 隐藏加载框
    fun hideLoading() {
        dismissDialog()
    }

    private fun dismissDialog() {
        if (null != tipDialog) {
            tipDialog!!.dismiss()
        }
    }

    fun showTip(
        context: Context?,
        iconType: Int,
        tipWord: String?
    ) {
        dismissDialog()
        tipDialog = QMUITipDialog.Builder(context)
            .setIconType(iconType)
            .setTipWord(tipWord)
            .create()
        tipDialog?.apply {
            window?.setGravity(Gravity.CENTER)
            setCancelable(false)
            show()
        }
        HandlerUtils.runOnUiThreadDelay(Runnable { tipDialog?.dismiss() }, DELAY_DISMISS)
    }

    fun showTip(context: Context?, tipWord: String?) {
        dismissDialog()
        tipDialog = QMUITipDialog.Builder(context)
            .setTipWord(tipWord)
            .create()
        tipDialog?.apply {
            window?.setGravity(Gravity.CENTER)
            setCancelable(false)
            show()
        }
        HandlerUtils.runOnUiThreadDelay(Runnable { tipDialog?.dismiss() }, DELAY_DISMISS)
    }

    // 显示成功提示框
    fun showTipSuccess(context: Context?, tipWord: String?) {
        showTip(context, QMUITipDialog.Builder.ICON_TYPE_SUCCESS, tipWord)
    }

    // 显示失败提示框
    fun showTipFail(context: Context?, tipWord: String?) {
        showTip(context, QMUITipDialog.Builder.ICON_TYPE_FAIL, tipWord)
    }

    // 显示信息提示框
    fun showTipInfo(context: Context?, tipWord: String?) {
        showTip(context, QMUITipDialog.Builder.ICON_TYPE_INFO, tipWord)
    }

    companion object {
        private const val DELAY_DISMISS = (1 * 1000).toLong()

        val instance: DialogUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DialogUtils()
        }

    }
}