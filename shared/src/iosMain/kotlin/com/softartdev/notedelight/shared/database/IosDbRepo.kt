package com.softartdev.notedelight.shared.database

import com.softartdev.notedelight.shared.PlatformSQLiteState
import com.softartdev.notedelight.shared.data.PlatformSQLiteThrowable
import kotlin.native.concurrent.freeze

class IosDbRepo : DatabaseRepo() {

    private var dbHolder: DatabaseHolder? = null

    override val databaseState: PlatformSQLiteState
        get() = TODO("remove or change on Realm")

    override fun buildDatabaseInstanceIfNeed(passphrase: CharSequence): DatabaseHolder {
        if (dbHolder != null) {
            return dbHolder!!
        }
        val passkey = if (passphrase.isEmpty()) null else passphrase.toString()
        dbHolder = IosDatabaseHolder(
            key = passkey,
            rekey = passkey
        )
        return dbHolder!!
    }

    override fun decrypt(oldPass: CharSequence) {
        closeDatabase()
//        IosCipherUtils.decrypt(oldPass.toString(), DB_NAME)TODO("remove or change on Realm")
        dbHolder = IosDatabaseHolder().freeze()
    }

    override fun rekey(oldPass: CharSequence, newPass: CharSequence) {
        closeDatabase()
        dbHolder = IosDatabaseHolder(
            key = oldPass.toString(),
            rekey = newPass.toString()
        ).freeze()
    }

    override fun encrypt(newPass: CharSequence) {
        closeDatabase()
//        IosCipherUtils.encrypt(newPass.toString(), DB_NAME)TODO("remove or change on Realm")
        dbHolder = IosDatabaseHolder(
            key = newPass.toString()
        ).freeze()
    }

    override fun closeDatabase() {
        dbHolder?.close()
        dbHolder = null
    }
}