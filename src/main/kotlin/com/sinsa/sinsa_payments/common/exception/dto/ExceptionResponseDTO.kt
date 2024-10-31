package com.sinsa.sinsa_payments.common.exception.dto

import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode

data class ExceptionResponseDTO<T> (
    val code: ExceptionCode? = null,
    val content: T?
)