package com.softartdev.notedelight.shared.database

import com.softartdev.notedelight.shared.date.DateAdapter
import com.softartdev.notedelight.shared.date.getSystemTimeInMillis
import com.softartdev.notedelight.shared.db.Note
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object TestSchema {//TODO remove or change on Realm
    private val ldt: LocalDateTime//FIXME cannot use createLocalDateTime() because nanoseconds don't save to db therefore tests fail
        get() = Instant.fromEpochMilliseconds(
            epochMilliseconds = getSystemTimeInMillis()
        ).toLocalDateTime(TimeZone.currentSystemDefault())

    val firstNote = Note(1, "first title from test schema", "first text", ldt, ldt)
    val secondNote = Note(2, "second title", "second text", ldt, ldt)
    val thirdNote = Note(3, "third title", "third text", ldt, ldt)
}
