package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointUseVO
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointSnapshotUseCase
import com.sinsa.sinsa_payments.api.out.adapter.FreePointCommandAdapter
import com.sinsa.sinsa_payments.api.out.adapter.FreePointInquiryAdapter
import com.sinsa.sinsa_payments.api.out.adapter.FreePointSnapshotCommandAdapter
import com.sinsa.sinsa_payments.common.exception.FreePointException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import com.sinsa.sinsa_payments.domain.FreePoint
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class FreePointSnapshotCommandService (
    private val freePointInquiryAdapter: FreePointInquiryAdapter,
    private val freePointCommandAdapter: FreePointCommandAdapter,
    private val freePointSnapshotCommandAdapter: FreePointSnapshotCommandAdapter
) : SaveFreePointSnapshotUseCase {

    @Transactional
    override fun save(freePointUseVO: FreePointUseVO) {
        val now = LocalDateTime.now()

        val manualFreePoints =
            freePointInquiryAdapter.findFreePointsByMemberIdAndManual(freePointUseVO.memberId, true, now)

        // 수기로 추가된 포인트 부터 사용한다.
        val remainManualPoint = useFreePoint(manualFreePoints, freePointUseVO.point, freePointUseVO.orderId)

        // 수기 지금건 만으로도 포인트 사용이 가능하다면 끝낸다.
        if (remainManualPoint == BigDecimal.ZERO) {
            return
        }

        // 수기 지급으로는 포인트 사용이 모자르다면 수기 포인트가 아닌 포인트로 포인트 사용을 한다.
        val noManualFreePoints =
            freePointInquiryAdapter.findFreePointsByMemberIdAndManual(freePointUseVO.memberId, false, now)

        val remainNoManualPoint = useFreePoint(noManualFreePoints, remainManualPoint, freePointUseVO.orderId)

        // 총 지급된 포인트 보다 사용하려는 포인트가 많은 경우 (없을 것이라 생각되지만 예외 처리)
        if (remainNoManualPoint > BigDecimal.ZERO) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_APPROVAL_TOO_MUCH,
                ExceptionCode.FREE_POINT_APPROVAL_TOO_MUCH.message
            )
        }
    }

    private fun useFreePoint(freePoints: List<FreePoint>, totalPoint: BigDecimal, orderId: String) : BigDecimal {
        var initTotalPoint = totalPoint

        freePoints.forEach {
            val currentPoint = it.point

            if(currentPoint <= initTotalPoint) {
                initTotalPoint = initTotalPoint.minus(currentPoint)
                freePointSnapshotCommandAdapter.save(FreePointSnapshot(
                    memberId = it.memberId,
                    pointId = it.id!!,
                    orderId = orderId,
                    point = currentPoint,
                    status = FreePointSnapshotStatus.APPROVAL
                ))

                it.usePoint(currentPoint)

                freePointCommandAdapter.save(it)
            }
            else {
                freePointSnapshotCommandAdapter.save(FreePointSnapshot(
                    memberId = it.memberId,
                    pointId = it.id!!,
                    orderId = orderId,
                    point = initTotalPoint,
                    status = FreePointSnapshotStatus.APPROVAL
                ))

                it.usePoint(initTotalPoint)

                freePointCommandAdapter.save(it)
                return BigDecimal.ZERO
            }
        }

        return initTotalPoint
    }
}