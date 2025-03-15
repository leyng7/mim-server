package com.mim.domain.chat

import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap


@Service
class WebSocketService(
    private val messagingTemplate: SimpMessagingTemplate
) {
    private val userSessionMap = ConcurrentHashMap<String, String>() // userId to sessionId
    private val sessionUserMap = ConcurrentHashMap<String, String>() // sessionId to userId

    fun registerUserSession(userId: String, sessionId: String) {
        userSessionMap[userId] = sessionId
        sessionUserMap[sessionId] = userId
    }

    fun removeUserSession(sessionId: String) {
        val userId = sessionUserMap[sessionId]
        if (userId != null) {
            userSessionMap.remove(userId)
            sessionUserMap.remove(sessionId)
        }
    }

    fun sendMessage(chatMessage: ChatMessage) {
        // 메시지를 특정 채팅방으로 전송
        messagingTemplate.convertAndSend(
            "/topic/chat.${chatMessage.roomId}",
            chatMessage
        )
    }

    fun notifyMatching(userId1: String, userId2: String, chatRoomId: String) {
        // WebSocket을 통해 매칭 결과를 사용자에게 알림
        val matchNotification = MatchNotification(chatRoomId, userId2)
        messagingTemplate.convertAndSendToUser(
            userId1,
            "/queue/match",
            matchNotification
        )

        val matchNotification2 = MatchNotification(chatRoomId, userId1)
        messagingTemplate.convertAndSendToUser(
            userId2,
            "/queue/match",
            matchNotification2
        )
    }

    data class MatchNotification(val roomId: String, val partnerId: String)

    fun createHeaders(sessionId: String?): MessageHeaders {
        val headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE)
        if (sessionId != null) headerAccessor.sessionId = sessionId
        headerAccessor.setLeaveMutable(true)
        return headerAccessor.messageHeaders
    }
}

data class ChatMessage(
    val roomId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
