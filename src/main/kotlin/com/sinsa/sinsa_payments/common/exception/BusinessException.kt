package com.sinsa.sinsa_payments.common.exception

import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode

sealed class BusinessException(
    open val exceptionCode: ExceptionCode,
    override val message: String?,
    override val cause: Throwable? = null
): RuntimeException(message, cause)