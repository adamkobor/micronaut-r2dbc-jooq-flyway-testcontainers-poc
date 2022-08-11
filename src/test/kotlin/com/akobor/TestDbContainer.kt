package com.akobor

import org.testcontainers.containers.PostgreSQLContainer

class TestDbContainer : PostgreSQLContainer<TestDbContainer>("postgres:13") {
    companion object {
        private lateinit var instance: TestDbContainer

        fun start() {
            if (!Companion::instance.isInitialized) {
                instance = TestDbContainer()
                instance.start()
                val r2dbcUrl = "r2dbc:postgresql://${instance.host}:${instance.firstMappedPort}"

                System.setProperty("r2dbc.datasources.default.url", r2dbcUrl)
                System.setProperty("r2dbc.datasources.default.username", instance.username)
                System.setProperty("r2dbc.datasources.default.password", instance.password)

                System.setProperty("flyway.datasources.default.url", instance.jdbcUrl)
                System.setProperty("flyway.datasources.default.username", instance.username)
                System.setProperty("flyway.datasources.default.password", instance.password)
            }
        }

        fun stop() {
            instance.stop()
        }
    }
}
