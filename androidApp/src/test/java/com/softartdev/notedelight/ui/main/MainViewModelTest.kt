package com.softartdev.notedelight.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.softartdev.notedelight.shared.data.NoteUseCase
import com.softartdev.notedelight.shared.db.Note
import com.softartdev.notedelight.shared.test.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val noteUseCase = Mockito.mock(NoteUseCase::class.java)
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(noteUseCase)
    }

    @Test
    fun success() = runBlocking {
        mainViewModel.resultStateFlow.test {
            assertEquals(NoteListResult.Loading, expectItem())

            val notes = emptyList<Note>()
            Mockito.`when`(noteUseCase.getNotes()).thenReturn(flowOf(notes))
            mainViewModel.updateNotes()
            assertEquals(NoteListResult.Success(notes), expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun navMain() = runBlocking {
        mainViewModel.resultStateFlow.test {
            assertEquals(NoteListResult.Loading, expectItem())

//            Mockito.`when`(noteUseCase.getNotes()).thenReturn(flow { throw SQLiteException() })TODO("remove or change on Realm")
            mainViewModel.updateNotes()
            assertEquals(NoteListResult.NavMain, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun error() = runBlocking {
        mainViewModel.resultStateFlow.test {
            assertEquals(NoteListResult.Loading, expectItem())

            Mockito.`when`(noteUseCase.getNotes()).thenReturn(flow { throw Throwable() })
            mainViewModel.updateNotes()
            assertEquals(NoteListResult.Error(null), expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}