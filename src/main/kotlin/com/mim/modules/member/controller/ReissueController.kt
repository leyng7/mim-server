package com.mim.modules.member.controller

import com.mim.modules.member.domain.Role
import com.mim.modules.member.jwt.CookieUtil
import com.mim.modules.member.jwt.JWTType
import com.mim.modules.member.jwt.JWTUtil
import com.mim.modules.member.repository.RefreshRepository
import io.jsonwebtoken.ExpiredJwtException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Tag(name = "토큰 관리")
@RestController
class ReissueController(
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RefreshRepository
) {

    @Operation(summary = "리프레시 토큰으로 새 토큰 발급")
    @PostMapping("/reissue")
    fun reissue(
        @Parameter(description = "리프레시 토큰")
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
        response.addCookie(
            CookieUtil.createCookie(
                name = JWTType.REFRESH_TOKEN.label,
                value = newRefreshToken,
                maxAge = tokenDuration,
                isSecure = false // 운영때는 true
            )
        )

        // TODO : 작업 필요
        refreshRepository.deleteByRefreshToken(refreshToken)
        // refreshRepository.save(newRefreshToken)

        return ResponseEntity.ok().body("")
    }

    @Operation(
        summary = "테스트용 토큰 발급",
        description = "테스트를 위한 Access Token과 Refresh Token을 발급합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "토큰 발급 성공",
                headers = [
                    Header(
                        name = HttpHeaders.AUTHORIZATION,
                        description = "Access Token (Bearer 형식)",
                        schema = Schema(type = "string")
                    )
                ]
            )
        ]
    )
    @PostMapping("/test-reissue")
    fun testReissue(
        @Parameter(description = "테스트 아이디(admin, admin1)")
        @RequestParam username: String,
        response: HttpServletResponse
    ): ResponseEntity<Void> {

         val role = Role.USER

        val newAccessToken = jwtUtil.createJwt(JWTType.ACCESS_TOKEN, username, role)
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer $newAccessToken")

        val tokenDuration = Duration.ofDays(1)
        val newRefreshToken = jwtUtil.createJwt(JWTType.REFRESH_TOKEN, username, role, tokenDuration)
        response.addCookie(
            CookieUtil.createCookie(
                name = JWTType.REFRESH_TOKEN.label,
                value = newRefreshToken,
                maxAge = tokenDuration,
                isSecure = false // 운영때는 true
            )
        )

        return ResponseEntity.ok().build()
    }

}
