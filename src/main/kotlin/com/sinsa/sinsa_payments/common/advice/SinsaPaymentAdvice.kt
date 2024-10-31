package com.sinsa.sinsa_payments.common.advice

import com.sinsa.sinsa_payments.common.exception.BusinessException
import com.sinsa.sinsa_payments.common.exception.dto.ExceptionResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SinsaPaymentAdvice {
    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(HttpStatus.OK)
    fun exceptionHandler(e: BusinessException): ExceptionResponseDTO<String> {
        return ExceptionResponseDTO(e.exceptionCode, e.message)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ExceptionResponseDTO<String> {
        e.cause?.let {
            return ExceptionResponseDTO(content = e.cause!!.message)
        }
        return ExceptionResponseDTO(content = e.message)
    }
}