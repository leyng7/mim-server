package com.mim.config

import com.mim.entity.Gender
import com.mim.entity.MBTI
import com.mim.entity.Role
import com.mim.entity.UserEntity
import com.mim.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class TestDataInitializer {

    @Bean
    fun initData(userRepository: UserRepository): CommandLineRunner {
        return CommandLineRunner {
            userRepository.save(
                UserEntity(
                    username = "admin",
                    name = "admin",
                    nickname = "admin",
                    mbti = MBTI.ESTJ,
                    dateOfBirth = LocalDate.of(1992, 8, 12),
                    gender = Gender.MALE,
                    role = Role.USER
                )
            )

            userRepository.save(
                UserEntity(
                    username = "admin2",
                    name = "admin",
                    nickname = "admin",
                    mbti = MBTI.ESTJ,
                    dateOfBirth = LocalDate.of(1992, 8, 12),
                    gender = Gender.MALE,
                    role = Role.USER
                )
            )
        }
    }

}
