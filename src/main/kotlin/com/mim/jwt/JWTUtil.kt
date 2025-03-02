package com.mim.jwt

import com.mim.entity.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtil(
    @Value("\${jwt.secret}")
    private val secret: String
) {

    private val secretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)

    fun getUsername(token: String): String {
        return parseToken(token).get("username", String::class.java)
    }

    fun getRole(token: String): Role {
        return Role.valueOf(parseToken(token).get("role", String::class.java))
    }

    fun isExpired(token: String): Boolean {
        return parseToken(token).expiration.before(Date.from(Instant.now()))
    }

    fun getType(token: String): JWTType {
        return JWTType.fromLabel(parseToken(token).get("type", String::class.java))
    }

    fun createJwt(
        type: JWTType,
        username: String,
        role: Role,
        duration: Duration = Duration.ofDays(1)
    ): String {
        val now = Instant.now()
        return Jwts.builder()
            .claim("type", type.label)
            .claim("username", username)
            .claim("role", role.name)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(duration)))
            .signWith(secretKey)
            .compact()
    }

    private fun parseToken(token: String): Claims {
        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).payload
    }


}
