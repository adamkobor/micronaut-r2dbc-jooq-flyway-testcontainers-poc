package com.akobor

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("/coroutine/accounts")
class CoroutineAccountController(private val accountRepository: CoroutineAccountRepository) :
    CoroutineAccountOperations {

    override suspend fun getAccounts(): List<AccountDetailsWithAddress> = accountRepository.getAccounts()

    override suspend fun getAccountById(accountId: Long): AccountDetailsWithAddress? =
        accountRepository.getAccount(accountId)

    override suspend fun createAccount(createDto: AccountCreateDto): AccountDetailsWithAddress? =
        accountRepository.insertAccountWithAddress(createDto)
}

interface CoroutineAccountOperations {

    @Get("/")
    suspend fun getAccounts(): List<AccountDetailsWithAddress>

    @Get("/{accountId}")
    suspend fun getAccountById(accountId: Long): AccountDetailsWithAddress?

    @Post("/")
    suspend fun createAccount(@Body createDto: AccountCreateDto): AccountDetailsWithAddress?
}
