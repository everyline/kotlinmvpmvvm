package com.base.commonlibrary.base

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/27
 *描述:TODO
 */
class BaseResponse<T>(val code: Int = 0, val msg: String? = null, val data: T? = null)