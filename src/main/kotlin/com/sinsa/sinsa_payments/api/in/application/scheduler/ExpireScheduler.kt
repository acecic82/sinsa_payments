package com.sinsa.sinsa_payments.api.`in`.application.scheduler

import com.sinsa.sinsa_payments.api.out.port.FindFreePointPort
import com.sinsa.sinsa_payments.api.out.port.SaveFreePointPort
import com.sinsa.sinsa_payments.api.out.port.SaveFreePointSnapshotPort
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class ExpireScheduler(
    private val findFreePointPort: FindFreePointPort,
    private val saveFreePointPort: SaveFreePointPort,
    private val saveFreePointSnapshotPort: SaveFreePointSnapshotPort
) {

    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional
    fun checkExpireFreePoint() {
        val now = LocalDateTime.now()
        val expiredFreePoint = findFreePointPort.findExpiredFreePoint(now)

        expiredFreePoint.forEach {
            saveFreePointSnapshotPort.save(
                FreePointSnapshot(
                    memberId = it.memberId,
                    pointId = it.id!!,
                    orderId = null,
                    point = -it.point,
                    approvalKey = null,
                    status = FreePointSnapshotStatus.EXPIRE
                )
            )

            it.usePoint(it.point)
            saveFreePointPort.save(it)
        }

    }
}