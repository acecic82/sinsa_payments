package com.sinsa.sinsa_payments.persistence.configuration

import mu.KLogging
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

internal class DataSourceRouter : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any =
        when (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            true -> TRANSACTION_READ
            else -> TRANSACTION_WRITE
        }

    companion object : KLogging() {
        const val TRANSACTION_READ = "read"
        const val TRANSACTION_WRITE = "write"
    }
}