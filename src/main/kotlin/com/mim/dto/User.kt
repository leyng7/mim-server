package com.mim.dto

import com.mim.entity.Role
import com.mim.entity.UserEntity

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
