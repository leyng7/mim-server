package com.mim.modules.chat.controller

import com.mim.infra.response.ErrorResponse
import com.mim.modules.chat.service.ChatMatchingService
import com.mim.modules.member.dto.LoginUser
import com.mim.modules.member.dto.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "랜덤 채팅")
@RestController
@RequestMapping("/chats")
class ChatController(
    private val chatMatchingService: ChatMatchingService
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
    fun requestRandomMatch(
        @LoginUser user: User
    ): ResponseEntity<Void> {
        chatMatchingService.addToWaitingQueue(user)
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

}
