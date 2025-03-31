package com.mim.modules.chat.repository

import com.mim.modules.chat.domain.ChatParticipant
import com.mim.modules.chat.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface ChatParticipantRepository : JpaRepository<ChatParticipant, Long> {

    fun findByChatRoom(chatRoom: ChatRoom): List<ChatParticipant>

    fun findByChatRoomAndUserId(chatRoom: ChatRoom, userId: Long): ChatParticipant?

    fun findAllByUserId(userId: Long): List<ChatParticipant>

    @Query("SELECT cp1.chatRoom " +
            "FROM ChatParticipant cp1 JOIN ChatParticipant cp2 ON cp1.chatRoom.id = cp2.chatRoom.id " +
            "WHERE cp1.userId = :userId AND cp2.userId = :otherMemberId AND cp1.chatRoom.isGroupChat is false")
    fun findExistingPrivateRoom(@Param("myId") userId: Long, @Param("otherMemberId") otherMemberId: Long): ChatRoom?

}
