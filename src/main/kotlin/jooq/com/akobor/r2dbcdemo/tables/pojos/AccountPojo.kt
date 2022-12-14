/*
 * This file is generated by jOOQ.
 */
package com.akobor.r2dbcdemo.tables.pojos


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.io.Serializable
import java.time.LocalDateTime


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "account",
    schema = "r2dbc-poc"
)
data class AccountPojo(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id", nullable = false)
    val id: Long? = null,
    @get:Column(name = "name", nullable = false, length = 255)
    @get:NotNull
    @get:Size(max = 255)
    val name: String? = null,
    @get:Column(name = "deleted_at", precision = 6)
    val deletedAt: LocalDateTime? = null
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other === null)
            return false
        if (this::class != other::class)
            return false
        val o: AccountPojo = other as AccountPojo
        if (this.id === null) {
            if (o.id !== null)
                return false
        }
        else if (this.id != o.id)
            return false
        if (this.name === null) {
            if (o.name !== null)
                return false
        }
        else if (this.name != o.name)
            return false
        if (this.deletedAt === null) {
            if (o.deletedAt !== null)
                return false
        }
        else if (this.deletedAt != o.deletedAt)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.id === null) 0 else this.id.hashCode())
        result = prime * result + (if (this.name === null) 0 else this.name.hashCode())
        result = prime * result + (if (this.deletedAt === null) 0 else this.deletedAt.hashCode())
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder("AccountPojo (")

        sb.append(id)
        sb.append(", ").append(name)
        sb.append(", ").append(deletedAt)

        sb.append(")")
        return sb.toString()
    }
}
