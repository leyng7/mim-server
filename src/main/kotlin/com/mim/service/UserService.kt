package com.mim.service

import com.mim.dto.ProfileRequest
import com.mim.entity.Role
import com.mim.entity.UserEntity
import com.mim.repository.UserRepository
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

}
