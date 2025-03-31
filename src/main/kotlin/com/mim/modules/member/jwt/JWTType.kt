package com.mim.modules.member.jwt

enum class JWTType(
    val label: String
) {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");

    companion object {
        fun fromLabel(label: String): JWTType = entries.find { it.label == label }
            ?: throw IllegalArgumentException("Unknown token type: $label")
    }
}
