package com.mim.modules.chat.repository

import com.mim.modules.chat.domain.ChatMessage
import com.mim.modules.chat.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository: JpaRepository<ChatMessage, Long> {

    fun findByChatRoomOrderByCreatedAt(chatRoom: ChatRoom): List<ChatMessage>

}
