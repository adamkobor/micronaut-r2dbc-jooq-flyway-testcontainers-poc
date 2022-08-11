package com.akobor

import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.time.LocalDateTime

@MicronautTest
class AccountControllerTest(accountClient: AccountClient) : DatabaseStringSpec({

    "getting all the accounts from the DB should work E2E" {

        val accounts = accountClient.getAccounts().collectList().block()!!

        accounts shouldHaveSize 3
        accounts.forOne { it.name shouldBe "Adam" }
        accounts.forOne { it.name shouldBe "Regina" }
        accounts.forOne { it.name shouldBe "John" }
    }

    "getting one specific account from the DB should work E2E" {

        val account = accountClient.getAccountById(1).block()!!

        account.id shouldBe 1
        account.deletedAt shouldBe null
        account.fullAddress shouldBe "Some Street in some city 12"
        account.name shouldBe "John"
    }

    "creating a new account should work E2E" {

        val accountWithAddress = AccountCreateDto(
            name = "Test Person 1",
            fullAddress = "Some address",
            deletedAt = LocalDateTime.now()
        )
        val accountWithoutAddress = AccountCreateDto(
            name = "Test Person 1",
            fullAddress = null,
            deletedAt = null
        )
        val createdAccountWithAddress = accountClient.createAccount(accountWithAddress).block()!!
        val createdAccountWithoutAddress = accountClient.createAccount(accountWithoutAddress).block()!!

        createdAccountWithAddress.id shouldNotBe null
        createdAccountWithAddress.name shouldBe accountWithAddress.name
        createdAccountWithAddress.fullAddress shouldBe accountWithAddress.fullAddress
        createdAccountWithAddress.deletedAt shouldBe accountWithAddress.deletedAt

        createdAccountWithoutAddress.id shouldNotBe null
        createdAccountWithoutAddress.name shouldBe accountWithoutAddress.name
        createdAccountWithoutAddress.fullAddress shouldBe null
        createdAccountWithoutAddress.deletedAt shouldBe null
    }
})

@Client("/accounts")
interface AccountClient : AccountOperations
