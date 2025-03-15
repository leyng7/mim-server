package com.mim.domain.chat

import com.mim.dto.CustomOAuth2User
import com.mim.dto.User
import com.mim.jwt.JWTType
import com.mim.jwt.JWTUtil
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
        val accessToken = servletRequest.getParameter("Authorization")?.replace("Bearer ", "") ?: return null

        try {
            jwtUtil.isExpired(accessToken)
        } catch (e: ExpiredJwtException) {
            println("token expired")
        }

        val type = jwtUtil.getType(accessToken)
        if (type != JWTType.ACCESS_TOKEN) {
            println("invalid access token")
        }

        val username = jwtUtil.getUsername(accessToken)
        val role = jwtUtil.getRole(accessToken)

        val user = User(username = username, name = "", role = role)
        val customOAuth2User = CustomOAuth2User(user)

        return UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
    }
}
