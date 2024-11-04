package com.sinsa.sinsa_payments

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SinsaPaymentsApplication

fun main(args: Array<String>) {
	runApplication<SinsaPaymentsApplication>(*args)
}
