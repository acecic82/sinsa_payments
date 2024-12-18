package com.sinsa.sinsa_payments.api.`in`.adapter.controller

import com.sinsa.sinsa_payments.api.`in`.adapter.dto.FreePointDTO
import com.sinsa.sinsa_payments.api.`in`.adapter.dto.FreePointTransactionDTO
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointSnapshotUseCase
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointUseCase
import com.sinsa.sinsa_payments.common.dto.SuccessResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/free-point")
class FreePointCommandController(
    private val saveFreePointUseCase: SaveFreePointUseCase,
    private val saveFreePointSnapshotUseCase: SaveFreePointSnapshotUseCase
) {

    @PostMapping("/save")
    fun save(
        @RequestBody freePointDTO: FreePointDTO
    ) : SuccessResponseDTO<FreePointDTO> {
        return SuccessResponseDTO.success(FreePointDTO.from(saveFreePointUseCase.save(freePointDTO.toVO())))
    }

    @PostMapping("/cancel")
    fun cancel(
        @RequestParam pointId: Long
    ) : SuccessResponseDTO<FreePointDTO> {
        saveFreePointUseCase.checkValidFreePointSnapshot(pointId)
        return SuccessResponseDTO.success(FreePointDTO.from(saveFreePointUseCase.cancel(pointId)))
    }

    @PostMapping("/use")
    fun use(
        @RequestBody freePointUseDTO : FreePointTransactionDTO
    ) : SuccessResponseDTO<Boolean> {
        saveFreePointSnapshotUseCase.use(freePointUseDTO.toVO())
        return SuccessResponseDTO.success(true)
    }

    @PostMapping("/use-cancel")
    fun useCancel(
        @RequestBody freePointUseDTO : FreePointTransactionDTO
    ) : SuccessResponseDTO<Boolean> {
        saveFreePointSnapshotUseCase.cancel(freePointUseDTO.toVO())
        return SuccessResponseDTO.success(true)
    }
}