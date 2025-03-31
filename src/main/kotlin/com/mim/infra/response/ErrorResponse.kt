package com.mim.infra.response

import io.swagger.v3.oas.annotations.media.Schema

/**
 * {
 *    "code": "400",
 *    "message": "잘못된 요청입니다.",
 *    "validation": {
 *      "title": "값을 입력해주세요"
 *    }
 * }
 */

@Schema(description = "에러 응답")
data class ErrorResponse(

    @Schema(description = "에러 코드", example = "400")
    val code: String,

    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
    val message: String,

    @Schema(
        description = "유효성 검사 오류 메시지",
        example = """{ "필드명": "오류 메시지", "필드명": "오류 메시지" }"""
    )
    val validation: MutableMap<String, String> = mutableMapOf()
) {

    fun addValidation(fieldName: String, errorMessage: String) {
        validation[fieldName] = errorMessage
    }

}

