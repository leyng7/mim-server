package com.mim.domain.chat

import com.mim.dto.LoginUser
import com.mim.dto.User
import com.mim.response.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@Tag(name = "랜덤 채팅")
@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatMatchingService: ChatMatchingService,
    private val webSocketService: WebSocketService
) {


    @Operation(
        summary = "랜덤 채팅 매칭 요청",
        description = "사용자를 랜덤 채팅 매칭 대기열에 추가합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "매칭 대기열 추가 성공"
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping("/random-match")
    fun requestRandomMatch(@LoginUser user: User, principal: Principal): ResponseEntity<Void> {
        chatMatchingService.addToWaitingQueue(ChatMatchingService.ChatUser(user.username))
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "랜덤 채팅 매칭 취소",
        description = "대기열에서 사용자를 제거합니다.",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "매칭 대기열 제거 성공"
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @DeleteMapping("/random-match")
    fun cancelRandomMatch(@LoginUser user: User): ResponseEntity<Void> {
        chatMatchingService.removeFromWaitingQueue(user.username)
        return ResponseEntity.ok().build()
    }

    // 테스트용 매칭 시뮬레이션 엔드포인트
    @PostMapping("/test-match")
    fun testMatch(@RequestParam partnerId: String, @LoginUser user: User): ResponseEntity<Map<String, String>> {
        val chatRoomId = UUID.randomUUID().toString()

        // 매칭 알림 전송
        webSocketService.notifyMatching(user.username, partnerId, chatRoomId)

        return ResponseEntity.ok(mapOf(
            "roomId" to chatRoomId,
            "partnerId" to partnerId
        ))
    }
}

@Controller
private class ChatMessageController(
    private val webSocketService: WebSocketService
) {

    @MessageMapping("/chat.send")
    @SendToUser("/queue/messages")
    fun sendMessage(
        @Payload chatMessage: ChatMessage,
        @Header("simpSessionId") sessionId: String
    ) {
        webSocketService.sendMessage(chatMessage)
    }
}
