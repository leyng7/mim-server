package com.mim.response

/**
 * {
 *    "code": "400",
 *    "message": "잘못된 요청입니다.",
 *    "validation": {
 *      "title": "값을 입력해주세요"
 *    }
 * }
 */

data class ErrorResponse(
    val code: String,
    val message: String,
    val validation: MutableMap<String, String> = mutableMapOf()
) {

    fun addValidation(fieldName: String, errorMessage: String) {
        validation[fieldName] = errorMessage
    }

}

