package com.mim.controller

import com.mim.dto.LoginUser
import com.mim.dto.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyController {

    @GetMapping("/my")
    fun myAPI(
        @LoginUser user: User,
    ): String {
        println(user)
        return "my route"
    }

}
