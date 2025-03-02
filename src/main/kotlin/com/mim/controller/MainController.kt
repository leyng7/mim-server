package com.mim.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
class MainController {

    @GetMapping("/")
    fun mainAPI(): String {
        return "main route"
    }

}
