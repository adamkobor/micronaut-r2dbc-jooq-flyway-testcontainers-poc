package com.akobor

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller("/accounts")
class AccountController(private val accountRepository: AccountRepository) : AccountOperations {

    override fun getAccounts(): Flux<AccountDetailsWithAddress> = accountRepository.getAccounts()

    override fun getAccountById(accountId: Long): Mono<AccountDetailsWithAddress> =
        accountRepository.getAccount(accountId)

    override fun createAccount(createDto: AccountCreateDto): Mono<AccountDetailsWithAddress> =
        accountRepository.insertAccountWithAddress(createDto)
}

interface AccountOperations {

    @Get("/")
    fun getAccounts(): Flux<AccountDetailsWithAddress>

    @Get("/{accountId}")
    fun getAccountById(accountId: Long): Mono<AccountDetailsWithAddress>

    @Post("/")
    fun createAccount(@Body createDto: AccountCreateDto): Mono<AccountDetailsWithAddress>
}
