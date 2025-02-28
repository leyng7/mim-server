package com.mim.controller

import com.mim.jwt.CookieUtil
import com.mim.jwt.JWTType
import com.mim.jwt.JWTUtil
import com.mim.repository.RefreshRepository
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
class ReissueController (
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RefreshRepository
) {

    @GetMapping("/reissue")
    fun reissue(
        @CookieValue(name = "refresh_token") refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<String> {

        try {
            jwtUtil.isExpired(refreshToken)
        } catch (e: ExpiredJwtException) {
            return ResponseEntity.badRequest().body("refresh token expired")
        }

        val type = jwtUtil.getType(refreshToken)
        if (type != JWTType.REFRESH_TOKEN) {
            return ResponseEntity.badRequest().body("invalid token type")
        }

        val isExist = refreshRepository.existsByRefreshToken(refreshToken)
        if (!isExist) {
            return ResponseEntity.badRequest().body("no refresh found")
        }

        val username = jwtUtil.getUsername(refreshToken)
        val role = jwtUtil.getRole(refreshToken)

        val newAccessToken = jwtUtil.createJwt(JWTType.ACCESS_TOKEN, username, role)
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer $newAccessToken")

        val tokenDuration = Duration.ofDays(1)
        val newRefreshToken = jwtUtil.createJwt(JWTType.REFRESH_TOKEN, username, role, tokenDuration)
        response.addCookie(CookieUtil.createCookie(
            name = JWTType.REFRESH_TOKEN.label,
            value = newRefreshToken,
            maxAge = tokenDuration,
            isSecure = false // 운영때는 true
        ))

        // TODO : 작업 필요
        refreshRepository.deleteByRefreshToken(refreshToken)
        // refreshRepository.save(newRefreshToken)

        return ResponseEntity.ok().body("")
    }

}
