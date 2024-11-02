package com.sinsa.sinsa_payments.api.`in`.adapter.controller

import com.sinsa.sinsa_payments.api.`in`.adapter.dto.FreePointDTO
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
    private val saveFreePointUseCase: SaveFreePointUseCase
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
        return SuccessResponseDTO.success(FreePointDTO.from(saveFreePointUseCase.cancel(pointId)))
    }
}