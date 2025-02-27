package com.mim.jwt

import com.mim.dto.CustomOAuth2User
import com.mim.dto.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTFilter(
    private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val cookies = request.cookies

        var authorization: String? = ""

        cookies.forEach {
            if (it.name == "Authorization") {
                authorization = it.value
            }
        }

        if (authorization.isNullOrBlank()) {
            println("token null")
            filterChain.doFilter(request, response)
            return
        }

        val token = authorization!!

        if (jwtUtil.isExpired(token)) {
            println("token expired")
            filterChain.doFilter(request, response)
            return
        }

        val username = jwtUtil.getUsername(token)
        val role = jwtUtil.getRole(token)

        val user = User(username = username, name = "", role = role)
        val customOAuth2User = CustomOAuth2User(user)

        val authenticationToken = UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
        SecurityContextHolder.getContext().authentication = authenticationToken

        filterChain.doFilter(request, response)
    }
}
