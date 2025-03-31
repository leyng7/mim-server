package com.mim.modules.member.domain

enum class MBTI(
    val label: String
) {
    ISTJ("ISTJ"), ISFJ("ISFJ"), INFJ("INFJ"), INTJ("INTJ"),
    ISTP("ISTP"), ISFP("ISFP"), INFP("INFP"), INTP("INTP"),
    ESTP("ESTP"), ESFP("ESFP"), ENFP("ENFP"), ENTP("ENTP"),
    ESTJ("ESTJ"), ESFJ("ESFJ"), ENFJ("ENFJ"), ENTJ("ENTJ"),
    NONE("선택안함")
}
