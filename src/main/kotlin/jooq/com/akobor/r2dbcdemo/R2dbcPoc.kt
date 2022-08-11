/*
 * This file is generated by jOOQ.
 */
package com.akobor.r2dbcdemo


import com.akobor.r2dbcdemo.sequences.ACCOUNT_ID_SEQ
import com.akobor.r2dbcdemo.sequences.ADDRESS_ID_SEQ
import com.akobor.r2dbcdemo.tables.Account
import com.akobor.r2dbcdemo.tables.Address

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Sequence
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class R2dbcPoc : SchemaImpl("r2dbc-poc", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>r2dbc-poc</code>
         */
        val `R2DBC-POC` : R2dbcPoc = R2dbcPoc()
    }

    /**
     * The table <code>r2dbc-poc.account</code>.
     */
    val ACCOUNT: Account get() = Account.ACCOUNT

    /**
     * The table <code>r2dbc-poc.address</code>.
     */
    val ADDRESS: Address get() = Address.ADDRESS

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getSequences(): List<Sequence<*>> = listOf(
        ACCOUNT_ID_SEQ,
        ADDRESS_ID_SEQ
    )

    override fun getTables(): List<Table<*>> = listOf(
        Account.ACCOUNT,
        Address.ADDRESS
    )
}