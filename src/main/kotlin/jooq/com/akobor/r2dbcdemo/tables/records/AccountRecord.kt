/*
 * This file is generated by jOOQ.
 */
package com.akobor.r2dbcdemo.tables.records


import com.akobor.r2dbcdemo.tables.Account
import com.akobor.r2dbcdemo.tables.pojos.AccountPojo

import java.time.LocalDateTime

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "account",
    schema = "r2dbc-poc"
)
open class AccountRecord() : UpdatableRecordImpl<AccountRecord>(Account.ACCOUNT), Record3<Long?, String?, LocalDateTime?> {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id", nullable = false, precision = 64)
    var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    @get:Column(name = "name", nullable = false, length = 255)
    @get:NotNull
    @get:Size(max = 255)
    var name: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    @get:Column(name = "deleted_at", precision = 6)
    var deletedAt: LocalDateTime?
        set(value): Unit = set(2, value)
        get(): LocalDateTime? = get(2) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row3<Long?, String?, LocalDateTime?> = super.fieldsRow() as Row3<Long?, String?, LocalDateTime?>
    override fun valuesRow(): Row3<Long?, String?, LocalDateTime?> = super.valuesRow() as Row3<Long?, String?, LocalDateTime?>
    override fun field1(): Field<Long?> = Account.ACCOUNT.ID
    override fun field2(): Field<String?> = Account.ACCOUNT.NAME
    override fun field3(): Field<LocalDateTime?> = Account.ACCOUNT.DELETED_AT
    override fun component1(): Long? = id
    override fun component2(): String? = name
    override fun component3(): LocalDateTime? = deletedAt
    override fun value1(): Long? = id
    override fun value2(): String? = name
    override fun value3(): LocalDateTime? = deletedAt

    override fun value1(value: Long?): AccountRecord {
        this.id = value
        return this
    }

    override fun value2(value: String?): AccountRecord {
        this.name = value
        return this
    }

    override fun value3(value: LocalDateTime?): AccountRecord {
        this.deletedAt = value
        return this
    }

    override fun values(value1: Long?, value2: String?, value3: LocalDateTime?): AccountRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        return this
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    constructor(id: Long? = null, name: String? = null, deletedAt: LocalDateTime? = null): this() {
        this.id = id
        this.name = name
        this.deletedAt = deletedAt
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    constructor(value: AccountPojo?): this() {
        if (value != null) {
            this.id = value.id
            this.name = value.name
            this.deletedAt = value.deletedAt
        }
    }
}