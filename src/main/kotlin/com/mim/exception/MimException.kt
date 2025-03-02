package com.mim.exception

abstract class MimException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val validation: MutableMap<String, String> = mutableMapOf()

    abstract val statusCode: Int

    fun addValidation(fieldName: String, message: String) {
        validation[fieldName] = message
    }
}
