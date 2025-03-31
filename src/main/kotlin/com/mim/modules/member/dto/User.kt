package com.mim.modules.member.dto

import com.mim.modules.member.domain.Role
import com.mim.modules.member.domain.UserEntity

data class User(
    val username: String,
    val name: String,
    val role: Role
) {

    constructor(userEntity: UserEntity) : this(
        username = userEntity.username,
        name = userEntity.name ?: "",
        role = userEntity.role
    )

}
