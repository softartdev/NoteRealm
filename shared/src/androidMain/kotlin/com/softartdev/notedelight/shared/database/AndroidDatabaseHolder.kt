package com.softartdev.notedelight.shared.database

import android.content.Context

class AndroidDatabaseHolder(
    context: Context,
    passphrase: CharSequence,
    name: String? = DatabaseRepo.DB_NAME
) : DatabaseHolder() {

    override fun close() = TODO("remove or change on Realm")
}