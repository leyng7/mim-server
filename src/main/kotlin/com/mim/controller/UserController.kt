package com.mim.controller

import com.mim.dto.LoginUser
import com.mim.dto.User
import com.mim.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

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
