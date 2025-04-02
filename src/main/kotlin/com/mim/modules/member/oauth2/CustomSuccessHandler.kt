package com.mim.modules.member.oauth2

import com.mim.modules.member.dto.CustomOAuth2User
import com.mim.modules.member.jwt.CookieUtil
import com.mim.modules.member.jwt.JWTType
import com.mim.modules.member.jwt.JWTUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CustomSuccessHandler(
    private val jwtUtil: JWTUtil,
    @Value("\${cors.allowed-origin}")
    private val allowedOrigin: String
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val customOAuth2User = authentication.principal as CustomOAuth2User
        val user = customOAuth2User.user
        val tokenDuration = Duration.ofDays(1)

        val token = jwtUtil.createJwt(
            type = JWTType.REFRESH_TOKEN,
            user = user,
            duration = tokenDuration
        )

        response.addCookie(
            CookieUtil.createCookie(
            name = JWTType.REFRESH_TOKEN.label,
            value = token,
            maxAge = tokenDuration,
            isSecure = false // 운영때는 true
        ))

        response.sendRedirect(allowedOrigin)
    }

}
