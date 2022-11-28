package com.akobor

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forNone
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.time.LocalDateTime

@MicronautTest
class CoroutineAccountControllerTest(
    accountClient: CoroutineAccountClient,
    accountRepository: CoroutineAccountRepository
) : DatabaseStringSpec({

    "getting all the accounts from the DB should work E2E" {

        val accounts = accountClient.getAccounts()

        accounts shouldHaveSize 3
        accounts.forOne { it.name shouldBe "Adam" }
        accounts.forOne { it.name shouldBe "Regina" }
        accounts.forOne { it.name shouldBe "John" }
    }

    "getting one specific account from the DB should work E2E" {

        val account = accountClient.getAccountById(1)!!

        account.id shouldBe 1
        account.deletedAt shouldBe null
        account.fullAddress shouldBe "Some Street in some city 12"
        account.name shouldBe "John"
    }

    "getting a non-existent account from the DB should work E2E" {

        val account = accountClient.getAccountById(11242)

        account shouldBe null
    }

    "creating a new account should work E2E" {

        val accountWithAddress = AccountCreateDto(
            name = "Test Person 1",
            fullAddress = "Some address",
            deletedAt = LocalDateTime.now()
        )
        val accountWithoutAddress = AccountCreateDto(
            name = "Test Person 2",
            fullAddress = null,
            deletedAt = null
        )
        val createdAccountWithAddress = accountClient.createAccount(accountWithAddress)!!
        val createdAccountWithoutAddress = accountClient.createAccount(accountWithoutAddress)!!

        createdAccountWithAddress.name shouldBe accountWithAddress.name
        createdAccountWithAddress.fullAddress shouldBe accountWithAddress.fullAddress
        createdAccountWithAddress.deletedAt shouldBe accountWithAddress.deletedAt

        createdAccountWithoutAddress.name shouldBe accountWithoutAddress.name
        createdAccountWithoutAddress.fullAddress shouldBe null
        createdAccountWithoutAddress.deletedAt shouldBe null

        val accountsInTheDb = accountRepository.getAccounts()
        accountsInTheDb.forOne { it.name shouldBe accountWithAddress.name }
        accountsInTheDb.forOne { it.name shouldBe accountWithoutAddress.name }
    }

    "if something bad happens during an account creation, it should be rolled back " {

        val accountToCreate = AccountCreateDto(
            name = "Test Person 1",
            fullAddress = "Some address".repeat(100), // We pass a very long string to force a DB related exception
            deletedAt = LocalDateTime.now()
        )

        shouldThrow<HttpClientResponseException> { accountClient.createAccount(accountToCreate) }

        val accountsInTheDb = accountRepository.getAccounts()

        accountsInTheDb.forNone { it.name shouldBe accountToCreate.name }
    }
})

@Client("/coroutine/accounts")
interface CoroutineAccountClient : CoroutineAccountOperations
