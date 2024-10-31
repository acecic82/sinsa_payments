package com.sinsa.sinsa_payments.common.exception

import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode

class PointPolicyException(
    override val exceptionCode: ExceptionCode,
    override val message: String?
) : BusinessException(exceptionCode, message)