package com.mim.modules.member.service

import com.mim.modules.member.domain.Role
import com.mim.modules.member.domain.UserEntity
import com.mim.modules.member.dto.ProfileRequest
import com.mim.modules.member.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
    private val userRepository: UserRepository
) {

    fun getUserEntity(username: String): UserEntity {
        return userRepository.findByUsername(username)
            ?: throw NoSuchElementException("No user found with username $username")
    }

    fun isProfileChanged(username: String): Boolean {
        val userEntity = getUserEntity(username)
        return userEntity.role == Role.USER
    }

    @Transactional
    fun updateProfile(username: String, request: ProfileRequest) {
        val userEntity = getUserEntity(username)
        userEntity.updateProfile(
            nickname = request.nickname,
            mbti = request.mbti,
            dateOfBirth = request.dateOfBirth,
            gender = request.gender
        )

    }

    @Transactional
    fun changeToUser(username: String) {
        val userEntity = getUserEntity(username)
        userEntity.changeToUser()
    }

}
