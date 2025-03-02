package com.mim.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDate

@Table(name = "tb_user")
@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val username: String,

    var name: String?,

    var nickname: String? = null,

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Enumerated(EnumType.STRING)
    var mbti: MBTI? = null,

    var dateOfBirth: LocalDate? = null,

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    var role: Role = Role.TEMP
) {

    fun changeName(name: String) {
        this.name = name
    }

    fun changeToUser() {
        this.role = Role.USER
    }

    fun updateProfile(
        nickname: String,
        mbti: MBTI,
        dateOfBirth: LocalDate,
        gender: Gender
    ) {
        this.nickname = nickname
        this.mbti = mbti
        this.dateOfBirth = dateOfBirth
        this.gender = gender
    }

}
