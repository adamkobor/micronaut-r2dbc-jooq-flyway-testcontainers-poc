/*
 * This file is generated by jOOQ.
 */
package com.akobor.r2dbcdemo.keys


import com.akobor.r2dbcdemo.tables.Account
import com.akobor.r2dbcdemo.tables.Address
import com.akobor.r2dbcdemo.tables.records.AccountRecord
import com.akobor.r2dbcdemo.tables.records.AddressRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val ACCOUNT_PKEY: UniqueKey<AccountRecord> = Internal.createUniqueKey(Account.ACCOUNT, DSL.name("account_pkey"), arrayOf(Account.ACCOUNT.ID), true)
val ADDRESS_PKEY: UniqueKey<AddressRecord> = Internal.createUniqueKey(Address.ADDRESS, DSL.name("address_pkey"), arrayOf(Address.ADDRESS.ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val ADDRESS__ADDRESS_ACCOUNT_ID_FKEY: ForeignKey<AddressRecord, AccountRecord> = Internal.createForeignKey(Address.ADDRESS, DSL.name("address_account_id_fkey"), arrayOf(Address.ADDRESS.ACCOUNT_ID), com.akobor.r2dbcdemo.keys.ACCOUNT_PKEY, arrayOf(Account.ACCOUNT.ID), true)
