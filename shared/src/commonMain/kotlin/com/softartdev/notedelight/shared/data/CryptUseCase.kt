package com.softartdev.notedelight.shared.data

import com.softartdev.notedelight.shared.PlatformSQLiteState
import com.softartdev.notedelight.shared.database.DatabaseRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.ExperimentalTime

class CryptUseCase(
        private val dbRepo: DatabaseRepo
) {
    fun dbIsEncrypted(): Boolean = when (dbRepo.databaseState) {
        PlatformSQLiteState.ENCRYPTED -> true
        PlatformSQLiteState.UNENCRYPTED -> false
        PlatformSQLiteState.DOES_NOT_EXIST -> false
    }

    @OptIn(ExperimentalTime::class)
    @Throws(Throwable::class) suspend fun isDbEncrypted(): Boolean = dbIsEncrypted()

    suspend fun checkPassword(pass: CharSequence): Boolean = try {
        dbRepo.closeDatabase()
        val passphrase = StringBuilder(pass) // threadsafe
        dbRepo.buildDatabaseInstanceIfNeed(passphrase)
        true
    } catch (t: Throwable) {
        t.printStackTrace()
        false
    }

    fun changePassword(oldPass: CharSequence?, newPass: CharSequence?) {
        if (dbIsEncrypted()) {
            requireNotNull(oldPass)
            if (newPass.isNullOrEmpty()) {
                dbRepo.decrypt(oldPass)
            } else {
                dbRepo.rekey(oldPass, newPass)
            }
        } else {
            requireNotNull(newPass)
            dbRepo.encrypt(newPass)
        }
        dbRepo.relaunchFlowEmitter?.invoke()
    }
}