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

    fun getAccounts(): Flux<AccountDetailsWithAddress> = Flux.from(
        ctx.select(
            ACCOUNT.ID,
            ACCOUNT.NAME,
            ACCOUNT.DELETED_AT,
            ADDRESS.FULL_ADDRESS
        ).from(ACCOUNT).leftJoin(ADDRESS).on(ADDRESS.ACCOUNT_ID.eq(ACCOUNT.ID))
    ).map { r -> r.into(AccountDetailsWithAddress::class.java) }

    fun getAccount(accountId: Long): Mono<AccountDetailsWithAddress> = ctx
        .select(
            ACCOUNT.ID,
            ACCOUNT.NAME,
            ACCOUNT.DELETED_AT,
            ADDRESS.FULL_ADDRESS
        ).from(ACCOUNT).leftJoin(ADDRESS).on(ADDRESS.ACCOUNT_ID.eq(ACCOUNT.ID))
        .where(ACCOUNT.ID.eq(accountId))
        .toMono().map { r -> r.into(AccountDetailsWithAddress::class.java) }

    fun insertAccountWithAddress(accountWithAddressDto: AccountCreateDto): Mono<AccountDetailsWithAddress> =
        ctx.insertInto(ACCOUNT)
            .columns(ACCOUNT.NAME, ACCOUNT.DELETED_AT)
            .values(accountWithAddressDto.name, accountWithAddressDto.deletedAt)
            .returningResult(ACCOUNT.ID)
            .toMono()
            .flatMap { insertedAccount ->
                if (!accountWithAddressDto.fullAddress.isNullOrBlank()) {
                    ctx.insertInto(ADDRESS)
                        .columns(ADDRESS.ACCOUNT_ID, ADDRESS.FULL_ADDRESS)
                        .values(insertedAccount.value1(), accountWithAddressDto.fullAddress)
                        .returningResult(ADDRESS.ACCOUNT_ID).toMono()
                } else insertedAccount.toMono()
            }.flatMap { insertedAccount -> getAccount(insertedAccount.value1()!!) }
}
