package com.mim.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyController {

    @GetMapping("/my")
    fun myAPI(): String {
        return "my route"
    }

}
