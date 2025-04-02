package com.mim.modules.chat.config

import com.mim.modules.member.dto.CustomOAuth2User
import com.mim.modules.member.jwt.JWTType
import com.mim.modules.member.jwt.JWTUtil
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal


@Component
class CustomHandshakeHandler(
    private val jwtUtil: JWTUtil
) : DefaultHandshakeHandler() {

    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal? {
        val servletRequest = (request as ServletServerHttpRequest).servletRequest

        val cookies = servletRequest.cookies

        var refreshToken = ""

        cookies.forEach {
            if (it.name == "refresh_token") {
                refreshToken = it.value
            }
        }

        if (refreshToken.isBlank()) {
            println("token null")
            return null
        }

        try {
            jwtUtil.isExpired(refreshToken)
        } catch (e: ExpiredJwtException) {
            println("token expired")
        }

        val type = jwtUtil.getType(refreshToken)
        if (type != JWTType.REFRESH_TOKEN) {
            println("invalid token type")
        }

        val user = jwtUtil.getUser(refreshToken)
        val customOAuth2User = CustomOAuth2User(user)

        return UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
    }
}
