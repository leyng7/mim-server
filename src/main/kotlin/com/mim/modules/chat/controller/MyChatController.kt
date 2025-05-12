package com.mim.modules.chat.controller

import com.mim.modules.chat.dto.MyChatListResponse
import com.mim.modules.chat.service.ChatService
import com.mim.modules.member.dto.LoginUser
import com.mim.modules.member.dto.User
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Tag(name = "랜덤 채팅")
@RestController
@RequestMapping("/my-chats")
class MyChatController(
    private val chatService: ChatService
) {

    @GetMapping
    fun myChats(@LoginUser user: User): List<MyChatListResponse> {
        return chatService.getMyChatRooms(user.id)
    }
}
