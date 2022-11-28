import org.jooq.meta.jaxb.MatcherRule
import org.jooq.meta.jaxb.MatcherTransformType
import org.jooq.meta.jaxb.Matchers
import org.jooq.meta.jaxb.MatchersTableType

buildscript {
    configurations["classpath"].resolutionStrategy.eachDependency {
        if (requested.group == "org.jooq") {
            useVersion("3.17.5")
        }
    }
}

plugins {
    val kotlinVersion = "1.7.21"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.5.1"
    id("nu.studer.jooq") version "8.0"
}

version = "0.1"
group = "com.akobor"

val kotlinVersion = project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.7")
    implementation("io.micronaut.sql:micronaut-jooq")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2") // You have to define this implicitly because jOOQ relies on them since 3.17.*
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0") // You have to define this implicitly because jOOQ relies on them since 3.17.*
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    jooqGenerator("org.postgresql:postgresql:42.3.3")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("org.testcontainers:testcontainers:1.17.6")
}


application {
    mainClass.set("com.akobor.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    version(project.properties["micronautVersion"].toString())
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.akobor.*")
    }
}

val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/postgres"
val dbUser = System.getenv("DB_USER") ?: "postgres"
val dbPassword = System.getenv("DB_PASSWORD") ?: "pass"
val dbSchema = "r2dbc-poc"
val dbDriver = "org.postgresql.Driver"
val jooqVersion = project.properties["jooqVersion"].toString()

jooq {
    version.set(jooqVersion)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                jdbc.apply {
                    driver = dbDriver
                    url = dbUrl
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        inputSchema = dbSchema
                        excludes = "flyway_schema_history"
                    }
                    generate.apply {
                        isDeprecated = false
                        isValidationAnnotations = true
                        isJpaAnnotations = true
                        isImmutablePojos = true
                    }
                    target.apply {
                        directory = "src/main/kotlin/jooq"
                        packageName = "com.akobor.r2dbcdemo"
                    }
                    strategy.apply {
                        name = "PojoSuffixStrategy"
                        matchers = Matchers().apply {
                            tables = listOf(
                                MatchersTableType().apply {
                                    pojoClass = MatcherRule().apply {
                                        transform = MatcherTransformType.PASCAL
                                        expression = "\$0_Pojo"
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
