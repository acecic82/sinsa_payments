package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import com.sinsa.sinsa_payments.persistence.repository.configuration.TestConfig
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeIn
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig::class)
class FreePointRepositoryTest(
    private val freePointRepository: FreePointRepository
) : DescribeSpec() {

    init {
        extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

        val memberIdList = listOf(1L,1L,2L,3L)
        val freePointListBeforeOneDay = memberIdList.map {
            FreePointEntity(
                memberId = it,
                point = BigDecimal(1000L),
                manual = false,
                expiredDate = LocalDateTime.now().minusDays(1L)
            )
        }

        val freePointListBeforeTwoDay = memberIdList.map {
            FreePointEntity(
                memberId = it,
                point = BigDecimal(1000L),
                manual = false,
                expiredDate = LocalDateTime.now().minusDays(2L)
            )
        }

        this.beforeSpec {
            freePointListBeforeOneDay.forEach {
                freePointRepository.save(it)
            }
            freePointListBeforeTwoDay.forEach {
                freePointRepository.save(it)
            }
        }

        this.describe("FreePointRepository Test") {
            context("memberId를 찾고 expiredDate 기준으로 정렬된 결과 테스트") {
                val memberId = 1L

                it("현재 시간 기준으로 검색시 전체가 정렬된 결과가 나와야 한다.") {
                    val now = LocalDateTime.now()

                    val realResult = freePointRepository.findPointByMemberId(memberId, now).map {
                        FreePointEntity(
                            it.id,
                            it.memberId,
                            BigDecimal.valueOf(it.point.toLong()),
                            it.manual,
                            it.expiredDate
                        )
                    }
                    val expectedResult =
                        (freePointListBeforeOneDay + freePointListBeforeTwoDay).filter { it.memberId == memberId }
                    var orderCheckItem = LocalDateTime.now().minusDays(3L)

                    realResult.forEachIndexed{ index, it ->
                        it shouldBeIn(expectedResult)

                        if (it.expiredDate.isAfter(orderCheckItem)) {
                            orderCheckItem = it.expiredDate
                        }
                        else {
                            throw AssertionError("결과가 올바르게 정렬되어 있지 않습니다.")
                        }
                    }
                }
            }
        }
    }
}