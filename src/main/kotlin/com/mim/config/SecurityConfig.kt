package com.mim.config

import com.mim.jwt.JWTCookieFilter
import com.mim.jwt.JWTUtil
import com.mim.oauth2.CustomSuccessHandler
import com.mim.service.CustomOAuth2UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customSuccessHandler: CustomSuccessHandler,
    private val jwtUtil: JWTUtil,
    @Value("\${cors.allowed-origin}")
    private val allowedOrigin: String
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .cors {
                it.configurationSource {
                    CorsConfiguration().apply {
                        allowedOrigins = listOf(allowedOrigin)
                        allowedMethods = listOf("*")
                        allowCredentials = true
                        allowedHeaders = listOf("*")
                        maxAge = 3600L
                        exposedHeaders = listOf("Set-Cookie", "Authorization")
                    }
                }
            }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .addFilterBefore(JWTCookieFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
            .oauth2Login {
                it.userInfoEndpoint { config ->
                    config.userService(customOAuth2UserService)
                }
                it.successHandler(customSuccessHandler)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/reissue").permitAll()
                    .requestMatchers("my").hasRole("USER")
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }

}
