package com.mim.modules.member.dto

import com.mim.modules.member.domain.Role
import com.mim.modules.member.domain.UserEntity

data class User(
    val id: Long,
    val username: String,
    val name: String,
    val role: Role
) {

    constructor(userEntity: UserEntity) : this(
        id = userEntity.id,
        username = userEntity.username,
        name = userEntity.name ?: "",
        role = userEntity.role
    )

}
