package com.mim.domain.chat

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventHandler(
    private val webSocketService: WebSocketService
) {
    private val logger = LoggerFactory.getLogger(WebSocketEventHandler::class.java)

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = headerAccessor.sessionId

        // 여기서 사용자 ID를 추출할 수 있습니다.
        // 예: 인증 정보에서 사용자 ID 추출
        // 테스트를 위해 세션 ID를 로그로 출력
        logger.info("WebSocket 연결 성공: sessionId={}", sessionId)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = headerAccessor.sessionId

        logger.info("WebSocket 연결 해제: sessionId={}", sessionId)

        // 세션 정보 제거
        if (sessionId != null) {
            webSocketService.removeUserSession(sessionId)
        }
    }
}
