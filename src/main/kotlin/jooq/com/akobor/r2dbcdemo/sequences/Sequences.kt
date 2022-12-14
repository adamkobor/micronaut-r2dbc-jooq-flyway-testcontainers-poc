/*
 * This file is generated by jOOQ.
 */
package com.akobor.r2dbcdemo.sequences


import org.jooq.Sequence
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType



/**
 * The sequence <code>r2dbc-poc.account_id_seq</code>
 */
val ACCOUNT_ID_SEQ: Sequence<Long> = Internal.createSequence("account_id_seq", com.akobor.r2dbcdemo.R2dbcPoc.`R2DBC-POC`, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null)

/**
 * The sequence <code>r2dbc-poc.address_id_seq</code>
 */
val ADDRESS_ID_SEQ: Sequence<Long> = Internal.createSequence("address_id_seq", com.akobor.r2dbcdemo.R2dbcPoc.`R2DBC-POC`, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null)
