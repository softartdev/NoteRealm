package com.softartdev.notedelight.shared.database

import com.softartdev.notedelight.shared.PlatformSQLiteState
import com.softartdev.notedelight.shared.data.PlatformSQLiteThrowable

/**
 * Encryption functions are mocked
 */
class JdbcDbTestRepo : DatabaseRepo() {
    @Volatile
    private var databaseHolder: DatabaseHolder? = buildDatabaseInstanceIfNeed()

    override val databaseState: PlatformSQLiteState
        get() = TODO("Not yet implemented")

    override fun buildDatabaseInstanceIfNeed(
        passphrase: CharSequence
    ): DatabaseHolder = synchronized(this) {
        var instance = databaseHolder
        if (instance == null) {
            instance = JdbcDatabaseTestHolder()
            databaseHolder = instance
        }
        return instance
    }

    override fun decrypt(oldPass: CharSequence) {
        closeDatabase()
        buildDatabaseInstanceIfNeed()
    }

    override fun rekey(oldPass: CharSequence, newPass: CharSequence) {
        closeDatabase()
        buildDatabaseInstanceIfNeed(newPass)
    }

    override fun encrypt(newPass: CharSequence) {
        closeDatabase()
        buildDatabaseInstanceIfNeed(newPass)
    }

    override fun closeDatabase() = synchronized(this) {
        databaseHolder?.close()
        databaseHolder = null
    }
}