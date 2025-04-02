package com.mim.modules.member.jwt

import com.mim.modules.member.dto.CustomOAuth2User
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTHeaderFilter(
    private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorization == null) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = authorization.replace("Bearer ", "")
        try {
            jwtUtil.isExpired(accessToken)
        } catch (e: ExpiredJwtException) {
            println("token expired")
            filterChain.doFilter(request, response)
            return
        }

        val type = jwtUtil.getType(accessToken)
        if (type != JWTType.ACCESS_TOKEN) {
            println("invalid access token")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val user = jwtUtil.getUser(accessToken)
        val customOAuth2User = CustomOAuth2User(user)

        val authenticationToken = UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.authorities)
        SecurityContextHolder.getContext().authentication = authenticationToken

        filterChain.doFilter(request, response)
    }
}
