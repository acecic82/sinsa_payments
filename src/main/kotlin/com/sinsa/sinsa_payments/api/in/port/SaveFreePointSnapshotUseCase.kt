package com.sinsa.sinsa_payments.api.`in`.port

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointTransactionVO

interface SaveFreePointSnapshotUseCase {
    fun save(freePointUseVO: FreePointTransactionVO)

    fun cancel(freePointTransactionVO: FreePointTransactionVO)
}