package com.softartdev.notedelight.shared.database

class IosDatabaseHolder(
    key: String? = null,
    rekey: String? = null,
    name: String = DatabaseRepo.DB_NAME,
) : DatabaseHolder() {

    override fun close() = TODO("remove or change on Realm")
}