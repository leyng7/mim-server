package com.mim.exception

class InvalidRequest : MimException("잘못된 요청입니다.") {

    override val statusCode: Int
        get() = 400

}
