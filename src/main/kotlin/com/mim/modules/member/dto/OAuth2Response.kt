package com.mim.modules.member.dto

interface OAuth2Response {

    // 제공자 (Ex. naver, google, ...)
    val provider: String

    // 제공자에서 발급해주는 아이디(번호)
    val providerId: String

    // 이메일
    val email: String

    // 사용자 실명 (설정한 이름)
    val name: String

}
