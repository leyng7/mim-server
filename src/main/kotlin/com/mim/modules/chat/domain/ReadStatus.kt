package com.mim.modules.chat.domain

import com.mim.modules.common.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "tb_read_status")
@Entity
class ReadStatus(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "read_status_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    val chatRoom: ChatRoom,

    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    val chatMessage: ChatMessage,

    var isRead: Boolean

) : BaseTimeEntity() {

    fun updateIsRead(isRead: Boolean) {
        this.isRead = isRead;
    }


}
