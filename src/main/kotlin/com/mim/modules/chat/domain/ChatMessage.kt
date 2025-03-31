package com.mim.modules.chat.domain

import com.mim.modules.common.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "tb_chat_message")
@Entity
class ChatMessage(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    val chatRoom: ChatRoom,

    val userId: Long,

    val content: String

) : BaseTimeEntity() {

}
