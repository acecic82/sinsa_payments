package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FreePointRepository : JpaRepository<FreePointEntity, Long>, FreePointRepositoryCustom {
}

interface  FreePointRepositoryCustom {

}