package com.akobor

import com.akobor.r2dbcdemo.tables.Account.Companion.ACCOUNT
import com.akobor.r2dbcdemo.tables.Address.Companion.ADDRESS
import jakarta.inject.Singleton
import org.jooq.DSLContext
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Singleton
class AccountRepository(private val ctx: DSLContext) {

    fun getAccounts(): Flux<AccountDetailsWithAddress> =
        Flux.from(ctx.getAccountQuery()).map { r -> r.into(AccountDetailsWithAddress::class.java) }

    private fun DSLContext.getAccountQuery() =
        select(
            ACCOUNT.ID,
            ACCOUNT.NAME,
            ACCOUNT.DELETED_AT,
            ADDRESS.FULL_ADDRESS
        ).from(ACCOUNT).leftJoin(ADDRESS).on(ADDRESS.ACCOUNT_ID.eq(ACCOUNT.ID))

    fun getAccount(accountId: Long, ctx: DSLContext = this.ctx): Mono<AccountDetailsWithAddress> = ctx
        .getAccountQuery()
        .where(ACCOUNT.ID.eq(accountId))
        .toMono().map { r -> r.into(AccountDetailsWithAddress::class.java) }

    fun insertAccountWithAddress(accountWithAddressDto: AccountCreateDto): Mono<AccountDetailsWithAddress> =
        // We initiate a transaction with jOOQ's new reactive transaction API
        ctx.transactionPublisher { trx ->
            trx.dsl().insertInto(ACCOUNT)
                .columns(ACCOUNT.NAME, ACCOUNT.DELETED_AT)
                .values(accountWithAddressDto.name, accountWithAddressDto.deletedAt)
                .returningResult(ACCOUNT.ID)
                .toMono()
                .flatMap { insertedAccount ->
                    if (!accountWithAddressDto.fullAddress.isNullOrBlank()) {
                        trx.dsl().insertInto(ADDRESS)
                            .columns(ADDRESS.ACCOUNT_ID, ADDRESS.FULL_ADDRESS)
                            .values(insertedAccount.value1(), accountWithAddressDto.fullAddress)
                            .returningResult(ADDRESS.ACCOUNT_ID).toMono()
                    } else insertedAccount.toMono()
                }.flatMap { insertedAccount -> getAccount(insertedAccount.value1()!!, trx.dsl()) }
        }.toMono()
}
