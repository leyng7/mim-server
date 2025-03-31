package com.mim.modules.member.controller

import com.mim.modules.member.dto.LoginUser
import com.mim.modules.member.dto.User
import com.mim.modules.member.service.UserService
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/users")
    fun updateProfile(
        @LoginUser user: User
    ) {
        println(user)
    }

}
