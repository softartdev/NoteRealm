package com.softartdev.notedelight.shared.data

import com.softartdev.notedelight.shared.PlatformSQLiteState
import com.softartdev.notedelight.shared.database.AndroidDbRepo
import com.softartdev.notedelight.shared.test.util.StubEditable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

@Suppress("IllegalIdentifier")
@OptIn(ExperimentalCoroutinesApi::class)
class CryptUseCaseUnitTest {

    private val mockDbRepo = Mockito.mock(AndroidDbRepo::class.java)
    private val cryptUseCase = CryptUseCase(mockDbRepo)

    @Test
    fun `check db state when db is encrypted`() {
        Mockito.`when`(mockDbRepo.databaseState).thenReturn(PlatformSQLiteState.ENCRYPTED)
        assertTrue(cryptUseCase.dbIsEncrypted())
    }

    @Test
    fun `check db state when db is unencrypted`() {
        Mockito.`when`(mockDbRepo.databaseState).thenReturn(PlatformSQLiteState.UNENCRYPTED)
        assertFalse(cryptUseCase.dbIsEncrypted())
    }

    @Test
    fun `check db state when db not exist`() {
        Mockito.`when`(mockDbRepo.databaseState).thenReturn(PlatformSQLiteState.DOES_NOT_EXIST)
        assertFalse(cryptUseCase.dbIsEncrypted())
    }

    @Test
    fun `check correct password`() = runBlocking {
//        Mockito.`when`(mockDbRepo.noteQueries).thenReturn(noteDb.noteQueries)TODO("remove or change on Realm")
        val pass = StubEditable("correct password")
        assertTrue(cryptUseCase.checkPassword(pass))
    }

    @Test
    fun `check incorrect password`() = runBlocking {
        val pass = StubEditable("incorrect password")
        assertFalse(cryptUseCase.checkPassword(pass))
    }

    @Test
    fun `change password for decrypt`() {
        Mockito.`when`(mockDbRepo.databaseState).thenReturn(PlatformSQLiteState.ENCRYPTED)
        val oldPass = StubEditable("old password")
        val newPass = null
        cryptUseCase.changePassword(oldPass, newPass)
        Mockito.verify(mockDbRepo).decrypt(oldPass)
    }

    @Test
    fun `change password for rekey`() {
        Mockito.`when`(mockDbRepo.databaseState).thenReturn(PlatformSQLiteState.ENCRYPTED)
        val oldPass = StubEditable("old password")
        val newPass = StubEditable("new password")
        cryptUseCase.changePassword(oldPass, newPass)
        Mockito.verify(mockDbRepo).rekey(oldPass, newPass)
    }

    @Test
    fun `change password for encrypt`() {
        Mockito.`when`(mockDbRepo.databaseState).thenReturn(PlatformSQLiteState.UNENCRYPTED)
        val oldPass = null
        val newPass = StubEditable("new password")
        cryptUseCase.changePassword(oldPass, newPass)
        Mockito.verify(mockDbRepo).encrypt(newPass)
    }

}