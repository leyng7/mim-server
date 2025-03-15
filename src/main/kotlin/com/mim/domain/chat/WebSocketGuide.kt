package com.mim.domain.chat

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "WebSocket 연결 가이드", description = "랜덤 채팅을 위한 WebSocket 클라이언트 가이드")
class WebSocketGuide {
    @Schema(description = "WebSocket 연결 URL")
    val connectUrl = "/ws-chat"

    @Schema(description = "매칭 결과 구독 경로")
    val matchSubscribeUrl = "/user/queue/match"

    @Schema(description = "채팅 메시지 구독 경로")
    val chatSubscribeUrl = "/topic/chat.{roomId}"

    @Schema(description = "채팅 메시지 전송 경로")
    val sendMessageUrl = "/publish/chat.send"

    @Schema(description = "채팅 메시지 전송 예제")
    val messageExample = """
    {
      "roomId": "room-uuid",
      "senderId": "user-id",
      "content": "안녕하세요!"
    }
    """
}
