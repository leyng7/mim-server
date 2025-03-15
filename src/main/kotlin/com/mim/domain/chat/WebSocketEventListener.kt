package com.mim.domain.chat

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionSubscribeEvent

@Component
class WebSocketEventListener {
    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleSubscribeEvent(event: SessionSubscribeEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val destination = accessor.destination
        val sessionId = accessor.sessionId
        val user = accessor.user?.name ?: "익명 사용자"

        logger.info("[구독] 사용자: {}, 세션: {}, 주소: {}", user, sessionId, destination)
    }
}
