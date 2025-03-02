package com.mim.handler

import com.mim.exception.MimException
import com.mim.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun invalidRequestHandler(e: MethodArgumentNotValidException): ErrorResponse {
        val response = ErrorResponse(
            code = "400",
            message = "잘못된 요청입니다."
        )

        e.bindingResult.fieldErrors.forEach { fieldError ->
            response.addValidation(fieldError.field, fieldError.defaultMessage ?: "Invalid value")
        }

        return response
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedHandler(e: AccessDeniedException): ErrorResponse {
        val response = ErrorResponse(
            code = "403",
            message = "접근이 거부되었습니다."
        )

        return response
    }

    @ExceptionHandler(MimException::class)
    fun mimException(e: MimException): ResponseEntity<ErrorResponse> {
        val statusCode: Int = e.statusCode

        val body = ErrorResponse(
            code = statusCode.toString(),
            message = e.message ?: "",
            validation = e.validation
        )

        val response = ResponseEntity.status(statusCode)
            .body(body)

        return response
    }


    @ExceptionHandler(Exception::class)
    fun exception(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("예외발생", e)

        val body = ErrorResponse(
            code = "500",
            message = e.message ?: ""
        )

        val response = ResponseEntity.status(500)
            .body(body)

        return response
    }
}
