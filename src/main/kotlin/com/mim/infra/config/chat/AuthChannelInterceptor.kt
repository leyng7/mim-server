package com.mim.infra.config.chat

import com.mim.modules.member.jwt.JWTType
import com.mim.modules.member.jwt.JWTUtil
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component
import java.security.Principal


@Component
class AuthChannelInterceptor(
    private val jwtUtil: JWTUtil
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val headerAccessor = StompHeaderAccessor.wrap(message)

        if (StompCommand.CONNECT == headerAccessor.command) {
            println("authorization 토큰 유효성 검증")
            val authorization = headerAccessor.getFirstNativeHeader("Authorization")!!
            val accessToken = authorization.replace("Bearer ", "")

            try {
                jwtUtil.isExpired(accessToken)
            } catch (e: ExpiredJwtException) {
                println("token expired")
                throw e
            }

            val type = jwtUtil.getType(accessToken)
            if (type != JWTType.ACCESS_TOKEN) {
                throw IllegalStateException()
            }

            val username = jwtUtil.getUsername(accessToken)
            val role = jwtUtil.getRole(accessToken)

            headerAccessor.user = StompPrincipal(username)
        }

        return message
    }

}

data class StompPrincipal(
    private val name: String
) : Principal {
    override fun getName(): String {
        return name
    }
}
