package com.akobor

import com.akobor.r2dbcdemo.tables.Account.Companion.ACCOUNT
import com.akobor.r2dbcdemo.tables.Address.Companion.ADDRESS
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.Record
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

// Glue code that needs to be implemented once
suspend inline fun <reified T : Any, R : Record> Publisher<R>.fetchInto(): List<T> =
    Flux.from(this).map { r -> r.into(T::class.java) }.toList()

suspend inline fun <reified T : Any, R : Record> Publisher<R>.fetchOneInto(): T? =
    Mono.from(this).map { r -> r.into(T::class.java) }.awaitFirstOrNull()

suspend fun <T : Any> Flux<T>.toList(): List<T> = asFlow().toList()

suspend fun <R : Record> transactionWithSingleResult(
    ctx: DSLContext,
    transactional: (Configuration) -> Publisher<R>
): R = Flux.from(ctx.transactionPublisher { trx -> transactional.invoke(trx) }).awaitSingle()


@Singleton
class CoroutineAccountRepository(private val ctx: DSLContext) {

    suspend fun getAccounts(): List<AccountDetailsWithAddress> = ctx.getAccountQuery().fetchInto()

    private fun DSLContext.getAccountQuery() =
        select(
            ACCOUNT.ID,
            ACCOUNT.NAME,
            ACCOUNT.DELETED_AT,
            ADDRESS.FULL_ADDRESS
        ).from(ACCOUNT).leftJoin(ADDRESS).on(ADDRESS.ACCOUNT_ID.eq(ACCOUNT.ID))

    suspend fun getAccount(accountId: Long, ctx: DSLContext = this.ctx): AccountDetailsWithAddress? =
        ctx.getAccountQuery().where(ACCOUNT.ID.eq(accountId)).fetchOneInto()

    suspend fun insertAccountWithAddress(accountWithAddressDto: AccountCreateDto): AccountDetailsWithAddress? {
        val insertedAccount = transactionWithSingleResult(ctx) { trx ->
            trx.dsl()
                .insertInto(ACCOUNT)
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
                }
        }
        return getAccount(insertedAccount.value1()!!)
    }
}
