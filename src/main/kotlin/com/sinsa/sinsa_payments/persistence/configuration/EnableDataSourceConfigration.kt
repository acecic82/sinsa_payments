package com.sinsa.sinsa_payments.persistence.configuration

import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited


@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@MustBeDocumented
@Inherited
@Import(DataSourceConfig::class)
annotation class EnableDataSourceConfigration()
