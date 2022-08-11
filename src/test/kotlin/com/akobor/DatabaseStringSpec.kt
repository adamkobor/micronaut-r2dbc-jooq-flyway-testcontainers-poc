package com.akobor

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import jakarta.inject.Inject
import org.flywaydb.core.Flyway

abstract class DatabaseStringSpec(body: StringSpec.() -> Unit = {}) : StringSpec(body) {

    @Inject
    lateinit var flyway: Flyway

    override fun afterEach(testCase: TestCase, result: TestResult) {
        flyway.clean()
        flyway.migrate()
    }
}
