package com.sinsa.sinsa_payments.api.`in`.adapter.controller

import com.sinsa.sinsa_payments.api.`in`.adapter.dto.FreePointSnapshotDTO
import com.sinsa.sinsa_payments.api.`in`.adapter.dto.PointPolicyDTO
import com.sinsa.sinsa_payments.api.`in`.port.FindFreePointSnapshotUseCase
import com.sinsa.sinsa_payments.api.`in`.port.SavePointPolicyUseCase
import com.sinsa.sinsa_payments.common.dto.SuccessResponseDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class BackOfficeController (
    private val savePointPolicyUseCase: SavePointPolicyUseCase,
    private val findFreePointSnapshotUseCase: FindFreePointSnapshotUseCase
) {

    @PostMapping("/max-accumulated-point")
    fun setMaxAccumulatedPoint(
        @RequestParam maxAccumulatedPoint: Long
    ) : SuccessResponseDTO<PointPolicyDTO> {
        return SuccessResponseDTO.success(
            PointPolicyDTO.from(
                savePointPolicyUseCase.saveMaxAccumulatedPoint(
                    maxAccumulatedPoint
                )
            )
        )
    }

    @PostMapping("/max-held-point")
    fun setMaxHeldPoint(
        @RequestParam maxHeldPoint: Long
    ) : SuccessResponseDTO<PointPolicyDTO> {
        return SuccessResponseDTO.success(PointPolicyDTO.from(savePointPolicyUseCase.saveMaxHeldPoint(maxHeldPoint)))
    }

    @PostMapping("/day-of-expired-date")
    fun setDayOfExpiredDate(
        @RequestParam days: Long
    ) : SuccessResponseDTO<PointPolicyDTO> {
        return SuccessResponseDTO.success(PointPolicyDTO.from(savePointPolicyUseCase.saveExpiredDateOfDay(days)))
    }

    @GetMapping("/history")
    fun getFreePointHistory(
        @RequestParam pointId: Long
    ): SuccessResponseDTO<List<FreePointSnapshotDTO>> {
        val freePointSnapshot = findFreePointSnapshotUseCase.findByPointId(pointId).map {
            FreePointSnapshotDTO.from(it)
        }

        return SuccessResponseDTO.success(freePointSnapshot)
    }
}