package com.sinsa.sinsa_payments.api.`in`.port

import com.sinsa.sinsa_payments.api.`in`.application.vo.PointPolicyVO

interface SavePointPolicyUseCase {
    fun saveMaxAccumulatedPoint(point: Long) : PointPolicyVO

    fun saveMaxHeldPoint(point: Long) : PointPolicyVO

    fun saveExpiredDateOfDay(days: Long) : PointPolicyVO
}