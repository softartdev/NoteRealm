package com.softartdev.notedelight.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.softartdev.notedelight.shared.data.NoteUseCase
import com.softartdev.notedelight.shared.date.createLocalDateTime
import com.softartdev.notedelight.shared.db.Note
import com.softartdev.notedelight.shared.test.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val noteUseCase = Mockito.mock(NoteUseCase::class.java)
    private var noteViewModel = NoteViewModel(noteUseCase)

    private val id = 1L
    private val title: String = "title"
    private val text: String = "text"
    private val ldt: LocalDateTime = createLocalDateTime()
    private val note = Note().apply {
        this@apply.id = this@NoteViewModelTest.id
        this@apply.title = this@NoteViewModelTest.title
        this@apply.text = this@NoteViewModelTest.text
        this@apply.dateCreated = this@NoteViewModelTest.ldt
        this@apply.dateModified = this@NoteViewModelTest.ldt
    }
    private val titleChannel = Channel<String>()

    @Before
    fun setUp() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(noteUseCase.createNote()).thenReturn(id)
        Mockito.`when`(noteUseCase.loadNote(id)).thenReturn(note)
        Mockito.`when`(noteUseCase.saveNote(id, title, text)).thenReturn(note)
        Mockito.`when`(noteUseCase.titleChannel).thenReturn(titleChannel)
        Mockito.`when`(noteUseCase.deleteNote(id)).thenReturn(1)
    }

    @After
    fun tearDown() = mainCoroutineRule.runBlockingTest {
        noteViewModel.resetLoadingResult()
    }

    @Test
    fun createNote() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.createNote()
            assertEquals(NoteResult.Created(id), expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadNote() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.loadNote(id)
            assertEquals(NoteResult.Loaded(note), expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveNoteEmpty() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.saveNote("", "")
            assertEquals(NoteResult.Empty, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveNote() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.saveNote(title, text)
            assertEquals(NoteResult.Saved(title), expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun editTitle() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.editTitle()
            assertEquals(NoteResult.NavEditTitle(id), expectItem())

            titleChannel.send(title)
            assertEquals(NoteResult.TitleUpdated(title), expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteNote() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.deleteNote()
            assertEquals(NoteResult.Deleted, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun checkSaveChange() = runBlocking {
        Mockito.`when`(noteUseCase.isChanged(id, title, text)).thenReturn(true)
        Mockito.`when`(noteUseCase.isEmpty(id)).thenReturn(false)
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.checkSaveChange(title, text)
            assertEquals(NoteResult.CheckSaveChange, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun checkSaveChangeNavBack() = runBlocking {
        Mockito.`when`(noteUseCase.isChanged(id, title, text)).thenReturn(false)
        Mockito.`when`(noteUseCase.isEmpty(id)).thenReturn(false)
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.checkSaveChange(title, text)
            assertEquals(NoteResult.NavBack, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun checkSaveChangeDeleted() = runBlocking {
        Mockito.`when`(noteUseCase.isChanged(id, title, text)).thenReturn(false)
        Mockito.`when`(noteUseCase.isEmpty(id)).thenReturn(true)
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.checkSaveChange(title, text)
            assertEquals(NoteResult.Deleted, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveNoteAndNavBack() = runBlocking {
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.saveNoteAndNavBack(title, text)
            assertEquals(NoteResult.NavBack, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun doNotSaveAndNavBack() = runBlocking {
        Mockito.`when`(noteUseCase.isEmpty(id)).thenReturn(false)
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.doNotSaveAndNavBack()
            assertEquals(NoteResult.NavBack, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun doNotSaveAndNavBackDeleted() = runBlocking {
        Mockito.`when`(noteUseCase.isEmpty(id)).thenReturn(true)
        noteViewModel.resultStateFlow.test {
            assertEquals(NoteResult.Loading, expectItem())

            noteViewModel.setIdForTest(id)
            noteViewModel.doNotSaveAndNavBack()
            assertEquals(NoteResult.Deleted, expectItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun errorResult() {
        assertEquals(NoteResult.Error(null), noteViewModel.errorResult(Throwable()))
    }
}