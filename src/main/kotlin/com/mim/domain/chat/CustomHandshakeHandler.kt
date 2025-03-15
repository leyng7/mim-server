package com.mim.domain.chat

import com.mim.config.chat.StompPrincipal
import com.mim.jwt.JWTUtil
import org.springframework.http.server.ServerHttpRequest
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
        val token = attributes["jwtToken"] as? String

        if (token != null) {
            val username = jwtUtil.getUsername(token)
            return StompPrincipal(username) // ✅ StompPrincipal 설정
        }

        return null
    }
}
