package com.sinsa.sinsa_payments.persistence.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

const val PACKAGE_NAME = "com.sinsa.sinsa_payments"
@Configuration
@ConditionalOnClass(EnableDataSourceConfigration::class)
@EnableJpaRepositories(
    entityManagerFactoryRef = "sinsaEntityManagerFactory",
    transactionManagerRef = "sinsaTransactionManager",
    basePackages = [PACKAGE_NAME]
)
@EnableTransactionManagement
@EnableJpaAuditing
class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun sinsaDataSourceProperty() : HikariConfig {
        val result =  HikariConfig()
        return result
    }

    @Bean
    fun sinsaDataSource(): DataSource {
        val data = HikariDataSource(sinsaDataSourceProperty())
        return LazyConnectionDataSourceProxy(data)
    }

    @Bean
    fun sinsaEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        val properties = HashMap<String, String>()
        properties[AvailableSettings.USE_SECOND_LEVEL_CACHE] = "false"
        properties[AvailableSettings.USE_QUERY_CACHE] = "false"

        return builder.dataSource(sinsaDataSource()).packages(PACKAGE_NAME).properties(properties).persistenceUnit("sinsa").build()
    }

    @Primary
    @Bean("sinsaTransactionManager")
    fun sinsaTransactionManager(@Qualifier("sinsaEntityManagerFactory") factory: EntityManagerFactory) = JpaTransactionManager(factory)
}
