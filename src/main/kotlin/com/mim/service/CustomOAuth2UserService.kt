package com.mim.service

import com.mim.dto.CustomOAuth2User
import com.mim.dto.NaverResponse
import com.mim.dto.User
import com.mim.entity.UserEntity
import com.mim.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val oAuth2Response = when (registrationId) {
            "naver" -> NaverResponse(oAuth2User.attributes)
            else -> throw IllegalStateException()
        }


        val username = "${oAuth2Response.provider}_${oAuth2Response.providerId}"
        val userEntity = userRepository.findByUsername(username)?.apply {
            changeName(oAuth2Response.name)
        } ?: UserEntity(
            username = username,
            name = oAuth2Response.name
        )

        userRepository.save(userEntity)

        return CustomOAuth2User(User(userEntity))
    }
}
