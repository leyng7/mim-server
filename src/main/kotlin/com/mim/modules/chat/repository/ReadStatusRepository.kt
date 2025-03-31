package com.mim.modules.chat.repository

import com.mim.modules.chat.domain.ChatRoom
import com.mim.modules.chat.domain.ReadStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ReadStatusRepository : JpaRepository<ReadStatus, Long> {

    fun findAllByChatRoomAndUserId(chatRoom: ChatRoom, userId: Long): List<ReadStatus>
    fun countByChatRoomAndUserId(chatRoom: ChatRoom, userId: Long): Long

}
