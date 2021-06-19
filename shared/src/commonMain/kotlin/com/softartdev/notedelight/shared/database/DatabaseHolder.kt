package com.softartdev.notedelight.shared.database

abstract class DatabaseHolder {
    //TODO remove or change on Realm:
//    abstract val driver: SqlDriver
//    abstract val noteDb: NoteDb
//    abstract val noteQueries: NoteQueries

    abstract fun close()
}
