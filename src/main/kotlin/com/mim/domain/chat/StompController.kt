package com.mim.domain.chat

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
class StompController {

    @MessageMapping("/{roomId}") //클라이언트에서 특정 publish/roomId형태로 메시지를 발행시 MessageMapping 수신
    @SendTo("/topic/{roomId}")  //해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
    fun sendMessage(
        @DestinationVariable roomId: Long,
        message: ChatMessages
    ): ChatMessages {
        println(message);
        return message
    }

}

data class ChatMessages (
    val clientId: String = "",
    val content: String = "",
    val sendTime: LocalDateTime = LocalDateTime.now(),
)
