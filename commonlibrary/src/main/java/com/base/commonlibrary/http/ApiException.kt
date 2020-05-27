package com.base.commonlibrary.http

import java.lang.RuntimeException

/**
 *creatd by 天天挂线
 *email:746608431@qq.com
 *on 2020/5/27
 *描述:TODO
 */
class ApiException : RuntimeException {
    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))

    constructor(code: Int, message: String?) : super(Throwable(message)) {
        this.code = code
    }
}