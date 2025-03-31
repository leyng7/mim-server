package com.mim.modules.member.dto

class NaverResponse(
    attribute: Map<String, Any>
) : OAuth2Response {

    @Suppress("UNCHECKED_CAST")
    private val attribute: Map<String, Any> = attribute["response"] as Map<String, Any>

    override val provider: String
        get() = "naver"

    override val providerId: String
        get() = attribute["id"].toString()

    override val email: String
        get() = attribute["email"].toString()

    override val name: String
        get() = attribute["name"].toString()

}
