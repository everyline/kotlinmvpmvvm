package com.base.commonlibrary.basemvp

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/27
 *描述:TODO
 */
interface MVPIBaseView {
    fun showLoading()
    fun dismissLoading()
    fun showError(throwable: Throwable)
}