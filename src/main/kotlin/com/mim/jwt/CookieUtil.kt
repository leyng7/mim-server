package com.mim.jwt

import jakarta.servlet.http.Cookie
import java.time.Duration

object CookieUtil {
    fun createCookie(
        name: String,
        value: String,
        maxAge: Duration = Duration.ofDays(1),
        path: String = "/",
        isHttpOnly: Boolean = true,
        isSecure: Boolean = true
    ): Cookie {
        return Cookie(name, value).apply {
            this.maxAge = maxAge.toSeconds().toInt()
            this.path = path
            this.isHttpOnly = isHttpOnly
            this.domain = "mim.com"
            this.secure = isSecure
        }
    }
}
