package com.mim.modules.chat.controller

import com.mim.modules.chat.dto.ChatMessageCommand
import com.mim.modules.chat.service.ChatMessage
import com.mim.modules.chat.service.ChatService
import com.mim.modules.chat.service.WebSocketService
import org.springframework.messaging.handler.annotation.*
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
class StompController(
    private val webSocketService: WebSocketService,
    private val chatService: ChatService
) {

    @MessageMapping("/chat.send")
    @SendToUser("/queue/messages")
    fun sendMessage(
        @Payload chatMessage: ChatMessage,
        @Header("simpSessionId") sessionId: String
    ) {
        webSocketService.sendMessage(chatMessage)
    }

    @MessageMapping("/{roomId}") //클라이언트에서 특정 publish/roomId형태로 메시지를 발행시 MessageMapping 수신
    @SendTo("/topic/{roomId}")  //해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
    fun sendMessage(
        @DestinationVariable roomId: Long,
        message: ChatMessages,
        @Header("simpUser") user: java.security.Principal
    ): ChatMessages {
        println(message);

        // Save message to database
        val chatMessageCommand = ChatMessageCommand(
            senderId = user.name.toLong(),
            message = message.content
        )
        chatService.saveMessage(roomId, chatMessageCommand)

        return message
    }

}

data class ChatMessages(
    val clientId: String = "",
    val content: String = "",
    val sendTime: LocalDateTime = LocalDateTime.now(),
)
