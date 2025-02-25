package com.mim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MimServerApplication

fun main(args: Array<String>) {
    runApplication<MimServerApplication>(*args)
}
