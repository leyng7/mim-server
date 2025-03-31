package com.mim.modules.chat.dto

class MyChatListResponse(
    val roomId: Long,
    val roomName: String,
    val isGroupChat: Boolean,
    val unReadCount: Long
)
