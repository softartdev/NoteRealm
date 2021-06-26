package com.softartdev.notedelight.shared.database

import com.softartdev.notedelight.shared.db.Note
import io.realm.Realm
import io.realm.RealmConfiguration

abstract class DatabaseHolder {

    val configuration = RealmConfiguration(schema = setOf(Note::class))
    val realm = Realm.open(configuration)

    abstract fun close()
}
