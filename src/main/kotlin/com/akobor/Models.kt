package com.akobor

import java.time.LocalDateTime

data class AccountDetailsWithAddress(
    val id: Long,
    val name: String,
    val fullAddress: String?,
    val deletedAt: LocalDateTime?
)

data class AccountCreateDto(
    val name: String,
    val fullAddress: String?,
    val deletedAt: LocalDateTime?
)
