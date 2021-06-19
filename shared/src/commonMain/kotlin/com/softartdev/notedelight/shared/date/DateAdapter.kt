package com.softartdev.notedelight.shared.date

import kotlinx.datetime.*

class DateAdapter {//TODO remove or change on Realm

    fun encode(value: LocalDateTime): Long = value
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    fun decode(databaseValue: Long): LocalDateTime = Instant
        .fromEpochMilliseconds(epochMilliseconds = databaseValue)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}