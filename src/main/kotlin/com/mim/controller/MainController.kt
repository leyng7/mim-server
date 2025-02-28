package com.mim.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class MainController {

    @GetMapping("/")
    fun mainAPI(
        principal: Principal
    ): String {
        return "main route"
    }

}
