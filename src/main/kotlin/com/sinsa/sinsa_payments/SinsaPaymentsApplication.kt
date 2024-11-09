package com.sinsa.sinsa_payments

import com.sinsa.sinsa_payments.persistence.configuration.EnableDataSourceConfigration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(
	exclude = [
		DataSourceAutoConfiguration::class
	],
	scanBasePackages = ["com.sinsa.sinsa_payments"]
)
@EnableDataSourceConfigration
@EnableScheduling
class SinsaPaymentsApplication

fun main(args: Array<String>) {
	runApplication<SinsaPaymentsApplication>(*args)
}
