package com.mim.modules.chat.domain

import com.mim.modules.common.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "tb_chat_room")
@Entity
class ChatRoom(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    val id: Long = 0,

    val name: String,

    val isGroupChat: Boolean = false

) : BaseTimeEntity() {

}
