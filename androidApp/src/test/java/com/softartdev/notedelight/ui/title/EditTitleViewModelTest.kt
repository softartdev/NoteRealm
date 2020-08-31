package com.softartdev.notedelight.ui.title

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softartdev.notedelight.shared.data.NoteUseCase
import com.softartdev.notedelight.shared.db.Note
import com.softartdev.notedelight.shared.date.createLocalDateTime
import com.softartdev.notedelight.shared.test.util.MainCoroutineRule
import com.softartdev.notedelight.shared.test.util.assertValues
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
class EditTitleViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val noteUseCase = Mockito.mock(NoteUseCase::class.java)
    private val editTitleViewModel = EditTitleViewModel(noteUseCase)

    private val id = 1L
    private val title: String = "title"
    private val text: String = "text"
    private val ldt: LocalDateTime = createLocalDateTime()
    private val note = Note(id, title, text, ldt, ldt)
    private val titleChannel = Channel<String>()

    @Before
    fun setUp() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(noteUseCase.loadNote(id)).thenReturn(note)
        Mockito.`when`(noteUseCase.updateTitle(id, title)).thenReturn(1)
        Mockito.`when`(noteUseCase.titleChannel).thenReturn(titleChannel)
    }

    @After
    fun tearDown() = mainCoroutineRule.runBlockingTest {
        editTitleViewModel.resultLiveData.value = null
    }

    @Test
    fun loadTitle() = editTitleViewModel.resultLiveData.assertValues(
            EditTitleResult.Loading,
            EditTitleResult.Loaded(title)
    ) {
        editTitleViewModel.loadTitle(id)
    }

    @Test
    fun editTitleSuccess() = editTitleViewModel.resultLiveData.assertValues(
            EditTitleResult.Loading,
            EditTitleResult.Success
    ) {
        val exp = "new title"
        editTitleViewModel.editTitle(id, exp)
        val act = runBlocking { titleChannel.receive() }
        assertEquals(exp, act)
    }

    @Test
    fun editTitleEmptyTitleError() = editTitleViewModel.resultLiveData.assertValues(
            EditTitleResult.Loading,
            EditTitleResult.EmptyTitleError
    ) {
        editTitleViewModel.editTitle(id, "")
    }

    @Test
    fun errorResult() {
        assertEquals(EditTitleResult.Error("$id"), editTitleViewModel.errorResult(Throwable("$id")))
    }
}