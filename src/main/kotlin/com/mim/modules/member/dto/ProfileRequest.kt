package com.mim.modules.member.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.mim.modules.member.domain.Gender
import com.mim.modules.member.domain.MBTI
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "프로필 업데이트 요청")
data class ProfileRequest(
    @Schema(description = "닉네임", example = "몽친")
    val nickname: String,

    @Schema(
        description = "MBTI",
        example = "ESTJ"
    )
    val mbti: MBTI,

    @Schema(description = "생년월일", example = "1992-08-12")
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dateOfBirth: LocalDate,

    @Schema(
        description = "성별",
        example = "MALE"
    )
    val gender: Gender
)
