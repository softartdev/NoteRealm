package com.softartdev.notedelight.shared.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.softartdev.notedelight.shared.database.AndroidDbRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject

@MediumTest
@RunWith(AndroidJUnit4::class)
class CryptUseCaseInstrumentedTest {

    private val dbRepo by inject(AndroidDbRepo::class.java)
    private val cryptUseCase = CryptUseCase(dbRepo)
    private val password = "password"

    @Test
    fun cryptTest() = runBlocking {
        assertFalse(cryptUseCase.dbIsEncrypted())
        cryptUseCase.changePassword(null, password)
        assertTrue(cryptUseCase.dbIsEncrypted())
        dbRepo.closeDatabase()
        assertFalse(cryptUseCase.checkPassword("incorrect password"))
        assertTrue(cryptUseCase.checkPassword(password))
    }

}