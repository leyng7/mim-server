package com.mim.controller

import com.mim.service.UserService
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

    // update Profile
    fun updateProfile() {
        println("update profile")
    }

}
