package com.softartdev.notedelight.shared.db

import com.softartdev.notedelight.shared.date.createLocalDateTime
import io.realm.PrimaryKey
import io.realm.RealmObject
import kotlin.Long
import kotlin.String
import kotlinx.datetime.LocalDateTime

class Note : RealmObject {
    @PrimaryKey var id: Long = 0
    var title: String = ""
    var text: String = ""
    var dateCreated: LocalDateTime = createLocalDateTime()
    var dateModified: LocalDateTime = createLocalDateTime()
}
