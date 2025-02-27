package com.mim.oauth2

import com.mim.dto.CustomOAuth2User
import com.mim.jwt.JWTUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomSuccessHandler(
    private val jwtUtil: JWTUtil
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val customOAuth2User = authentication.principal as CustomOAuth2User
        val user = customOAuth2User.user
        val token = jwtUtil.createJwt(user.username, user.role, 60 * 60 * 60 * 60L)

        response.addCookie(createCookie(value = token))
        response.sendRedirect("http://127.0.0.1:3000/")
    }

    private fun createCookie(key: String = "Authorization", value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 60 * 60 * 60 * 60
        //cookie.setSecure(true);
        cookie.path = "/"
        cookie.isHttpOnly = true

        return cookie
    }
}
