# Micronaut R2DBC + jOOQ + Flyway + Testcontainers PoC

This PoC is about integrating jOOQ into Micronaut by using R2DBC and Reactor, while Flyway is still able to use JDBC to manage schema migrations.

This example uses:

- Micronaut 3.6.x (with the official `micronaut-r2dbc` module)
- PostgreSQL 13 (with the official `r2dbc-postgresql` driver)
- Reactor 3.x.x
- jOOQ 3.17.* (and Etienne Studer's great Gradle plugin for jOOQ)
- Flyway 8.5.x
- Testcontainers (to be able to test the integration E2E)

You can read a detailed explanation about this PoC on my blog: [https://akobor.me/posts/wiring-up-micronaut-jooq-flyway-and-testcontainers-with-r2dbc](https://akobor.me/posts/wiring-up-micronaut-jooq-flyway-and-testcontainers-with-r2dbc)
