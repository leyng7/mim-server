package com.mim.modules.chat.service

import com.mim.modules.chat.domain.ChatMessage
import com.mim.modules.chat.domain.ChatParticipant
import com.mim.modules.chat.domain.ChatRoom
import com.mim.modules.chat.domain.ReadStatus
import com.mim.modules.chat.dto.ChatMessageCommand
import com.mim.modules.chat.dto.ChatMessageResponse
import com.mim.modules.chat.dto.ChatRoomListResponse
import com.mim.modules.chat.dto.MyChatListResponse
import com.mim.modules.chat.repository.ChatMessageRepository
import com.mim.modules.chat.repository.ChatParticipantRepository
import com.mim.modules.chat.repository.ChatRoomRepository
import com.mim.modules.chat.repository.ReadStatusRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service

@Transactional
class ChatService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val readStatusRepository: ReadStatusRepository
) {
    fun saveMessage(roomId: Long, command: ChatMessageCommand) {
        val chatRoom = chatRoomRepository.findById(roomId).orElseThrow { EntityNotFoundException("room cannot be found") }

        val chatMessage = ChatMessage(
            chatRoom = chatRoom,
            userId = command.senderId,
            content = command.message
        )
        chatMessageRepository.save(chatMessage)


        // Save read status for all participants
        val chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom)
        chatParticipants.forEach { participant ->
            val readStatus = ReadStatus(
                chatRoom = chatRoom,
                userId = command.senderId,
                chatMessage = chatMessage,
                isRead = participant.userId == command.senderId
            )
            readStatusRepository.save(readStatus)
        }
    }

    fun createGroupRoom(chatRoomName: String, userId: Long) {

        val chatRoom = ChatRoom(name = chatRoomName, isGroupChat = true)
        chatRoomRepository.save(chatRoom)

        val chatParticipant = ChatParticipant(chatRoom = chatRoom, userId = userId)
        chatParticipantRepository.save(chatParticipant)
    }

    fun getGroupChatRooms(): List<ChatRoomListResponse> =
        chatRoomRepository.findByIsGroupChat(true).map { chatRoom ->
            ChatRoomListResponse(roomId = chatRoom.id, roomName = chatRoom.name)
        }

    fun addParticipantToGroupChat(roomId: Long, userId: Long) {
        val chatRoom = chatRoomRepository.findById(roomId).orElseThrow { EntityNotFoundException("room cannot be found") }

        require(chatRoom.isGroupChat) { "그룹채팅이 아닙니다." }

        if (chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId) == null) {
            addParticipantToRoom(chatRoom, userId)
        }
    }

    private fun addParticipantToRoom(chatRoom: ChatRoom, userId: Long) {
        val chatParticipant = ChatParticipant(chatRoom = chatRoom, userId = userId)
        chatParticipantRepository.save(chatParticipant)
    }

    fun getChatHistory(roomId: Long, userId: Long): List<ChatMessageResponse> {
        val chatRoom = chatRoomRepository.findById(roomId).orElseThrow { EntityNotFoundException("room cannot be found") }

        require(chatParticipantRepository.findByChatRoom(chatRoom).any { it.userId == userId }) {
            "본인이 속하지 않은 채팅방입니다."
        }

        return chatMessageRepository.findByChatRoomOrderByCreatedAt(chatRoom).map { chatMessage ->
            ChatMessageResponse(
                message = chatMessage.content,
                senderId = chatMessage.userId
            )
        }
    }

    fun messageRead(roomId: Long, userId: Long) {
        val chatRoom = chatRoomRepository.findById(roomId).orElseThrow { EntityNotFoundException("room cannot be found") }

        readStatusRepository.findAllByChatRoomAndUserId(chatRoom, userId)
            .forEach { it.updateIsRead(true) }
    }

    fun getMyChatRooms(userId: Long): List<MyChatListResponse> {

        return chatParticipantRepository.findAllByUserId(userId).map { chatParticipant ->
            val unreadCount = readStatusRepository.countByChatRoomAndUserId(chatParticipant.chatRoom, userId)
            MyChatListResponse(
                roomId = chatParticipant.chatRoom.id,
                roomName = chatParticipant.chatRoom.name,
                isGroupChat = chatParticipant.chatRoom.isGroupChat,
                unReadCount = unreadCount
            )
        }
    }

    fun leaveGroupChatRoom(roomId: Long, userId: Long) {
        val chatRoom = chatRoomRepository.findById(roomId).orElseThrow { EntityNotFoundException("room cannot be found") }

        require(chatRoom.isGroupChat) { "단체 채팅방이 아닙니다." }

        val chatParticipant = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId)
            ?: throw EntityNotFoundException("참여자를 찾을 수 없습니다.")

        chatParticipantRepository.delete(chatParticipant)

        if (chatParticipantRepository.findByChatRoom(chatRoom).isEmpty()) {
            chatRoomRepository.delete(chatRoom)
        }
    }

    fun getOrCreatePrivateRoom(userId: Long, otherMemberId: Long): Long {

        chatParticipantRepository.findExistingPrivateRoom(userId, otherMemberId)?.let {
            return it.id
        }

        val newRoom = ChatRoom(isGroupChat = false, name = "${userId}-${otherMemberId}")
        chatRoomRepository.save(newRoom)

        addParticipantToRoom(newRoom, userId)
        addParticipantToRoom(newRoom, otherMemberId)

        return newRoom.id
    }
}
