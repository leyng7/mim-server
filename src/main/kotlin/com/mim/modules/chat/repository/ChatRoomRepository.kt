package com.mim.modules.chat.repository

import com.mim.modules.chat.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {

    fun findByIsGroupChat(groupChat: Boolean): List<ChatRoom>
}
