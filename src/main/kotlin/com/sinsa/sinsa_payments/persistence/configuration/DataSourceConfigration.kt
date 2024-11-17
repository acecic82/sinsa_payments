package com.sinsa.sinsa_payments.persistence.configuration

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
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
    @ConfigurationProperties(prefix = "spring.datasource.write")
    fun writeDataSource() : HikariDataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read")
    fun readDataSource() : HikariDataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean
//    @Primary
    @DependsOn(value = ["writeDataSource", "readDataSource"])
    fun dataSource(
        @Qualifier("writeDataSource") writeDataSource: HikariDataSource,
        @Qualifier("readDataSource") readDataSource: HikariDataSource,
    ): DataSource {
        val dataSourceRouter = DataSourceRouter()

        val dataSourceMap : HashMap<Any, Any> = HashMap()
        dataSourceMap["write"] = writeDataSource
        dataSourceMap["read"] = readDataSource
        dataSourceRouter.setTargetDataSources(dataSourceMap)
        dataSourceRouter.setDefaultTargetDataSource(writeDataSource)

        return dataSourceRouter
    }

    @Bean
    @Primary
    @DependsOn(value = ["dataSource"])
    fun lazyConnectionDataSource(
        @Qualifier("dataSource") dataSource: DataSource
    ): DataSource {
        return LazyConnectionDataSourceProxy(dataSource)
    }

    @Bean
    @DependsOn(value = ["lazyConnectionDataSource"])
    fun sinsaEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("lazyConnectionDataSource") lazyConnectionDataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        val properties = HashMap<String, String>()
        properties[AvailableSettings.USE_SECOND_LEVEL_CACHE] = "false"
        properties[AvailableSettings.USE_QUERY_CACHE] = "false"

        return builder.dataSource(lazyConnectionDataSource).packages(PACKAGE_NAME).properties(properties).persistenceUnit("sinsa").build()
    }

    @Primary
    @Bean("sinsaTransactionManager")
    fun sinsaTransactionManager(@Qualifier("sinsaEntityManagerFactory") factory: EntityManagerFactory) = JpaTransactionManager(factory)
}
