package com.sinsa.sinsa_payments.common.dto

data class SuccessResponseDTO<T>(
    val content: T? = null
) {
    companion object {
        fun <T> success(content: T? = null): SuccessResponseDTO<T> {
            return SuccessResponseDTO(content = content)
        }
    }
}
