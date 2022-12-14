/*
 * This file is generated by jOOQ.
 */
package com.akobor.r2dbcdemo.indexes


import com.akobor.r2dbcdemo.tables.Address

import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val IDX_ADDRESS_ACCOUNT_ID: Index = Internal.createIndex(DSL.name("idx_address_account_id"), Address.ADDRESS, arrayOf(Address.ADDRESS.ACCOUNT_ID), false)
