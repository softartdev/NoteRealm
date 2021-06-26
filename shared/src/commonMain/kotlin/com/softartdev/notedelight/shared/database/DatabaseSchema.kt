package com.softartdev.notedelight.shared.database

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

    val firstNote = Note().apply {
        id = 1
        title = "first title from test schema"
        text = "first text"
        dateCreated = ldt
        dateModified = ldt
    }
    val secondNote = Note().apply {
        id = 2
        title = "second title"
        text = "second text"
        dateCreated = ldt
        dateModified = ldt
    }
    val thirdNote = Note().apply {
        id = 3
        title = "third title"
        text = "third text"
        dateCreated = ldt
        dateModified = ldt
    }
}
