package com.mim.domain.chat

import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class ChatMatchingService(
    private val webSocketService: WebSocketService
) {

    private val waitingQueue = ConcurrentLinkedQueue<ChatUser>()
    private val userMatches = ConcurrentHashMap<String, String>() // username -> chatRoomId

    // 대기열에 사용자 추가
    fun addToWaitingQueue(user: ChatUser) {
        // 이미 대기 중인지 확인
//        if (waitingQueue.any { it.username == user.username }) {
//            return
//        }

        waitingQueue.add(user)
        checkForMatches()
    }

    // 대기열에서 사용자 제거
    fun removeFromWaitingQueue(username: String) {
        waitingQueue.removeIf { it.username == username }
    }

    // 매칭 가능한 사용자 확인
    private fun checkForMatches() {
        if (waitingQueue.size < 2) return

        synchronized(waitingQueue) {
            if (waitingQueue.size >= 2) {
                val user1 = waitingQueue.poll()
                val user2 = waitingQueue.poll()

                if (user1 != null && user2 != null) {
                    createChatRoom(user1, user2)
                }
            }
        }
    }

    // 채팅방 생성 및 사용자 초대
    private fun createChatRoom(user1: ChatUser, user2: ChatUser) {
        println("매칭 성공: ${user1.username} - ${user2.username}")
        val roomId = UUID.randomUUID().toString()

        userMatches[user1.username] = roomId
        userMatches[user2.username] = roomId

        // WebSocket을 통해 매칭 성공 알림
        webSocketService.notifyMatching(user1.username, user2.username, roomId)
    }

    data class ChatUser(val username: String)
}
