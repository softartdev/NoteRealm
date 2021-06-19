package com.softartdev.notedelight.shared.data

import com.softartdev.notedelight.shared.BaseTest
import com.softartdev.notedelight.shared.database.TestSchema
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlin.test.*

class NoteUseCaseTest : BaseTest() {

    private val noteUseCase = NoteUseCase(dbRepo)

    private val notes = listOf(TestSchema.firstNote, TestSchema.secondNote, TestSchema.thirdNote)

    @BeforeTest
    fun setUp() = runTest {
        //TODO("remove or change on Realm")
    }

    @AfterTest
    fun tearDown() = runTest {
        //TODO("remove or change on Realm")
    }

    @Test
    fun getTitleChannel() = runTest {
        val act = "test title"
        val deferred = async { noteUseCase.titleChannel.receive() }
        noteUseCase.titleChannel.send(act)
        val exp = deferred.await()
        assertEquals(exp, act)
    }

    @Test
    fun getNotes() = runTest {
        assertEquals(notes, noteUseCase.getNotes().first())
    }

    @Test
    fun launchNotes() {
        val count: AtomicInt = atomic(0)
        noteUseCase.launchNotes(onSuccess = { actNotes ->
            count.incrementAndGet()
            println("launchNotes - #$count onSuccess = $actNotes")
            if (actNotes.isNotEmpty()) {
                println("launchNotes - #$count assertEquals")
                assertEquals(notes, actNotes)
            }
        }, onFailure = { throwable -> throw throwable })
    }

    @Test
    fun createNote() = runTest {
        assertEquals(notes.last().id.inc(), noteUseCase.createNote())
    }

    @Test
    fun saveNote() = runTest {
        val id: Long = 2
        val newTitle = "new title"
        val newText = "new text"
        noteUseCase.saveNote(id, newTitle, newText)
        val updatedNote = noteUseCase.loadNote(id)
        assertEquals(newTitle, updatedNote.title)
        assertEquals(newText, updatedNote.text)
    }

    @Test
    fun updateTitle() = runTest {
        val id: Long = 2
        val newTitle = "new title"
        assertEquals(1, noteUseCase.updateTitle(id, newTitle))
        val updatedNote = noteUseCase.loadNote(id)
        assertEquals(newTitle, updatedNote.title)
    }

    @Test
    fun loadNote() = runTest {
        val id: Long = 2
        val exp = notes.find { it.id == id }
        val act = noteUseCase.loadNote(id)
        assertEquals(exp, act)
    }

    @Test
    fun deleteNote() = runTest {
        val id: Long = 2
        assertEquals(1, noteUseCase.deleteNote(id))
    }

    @Test
    fun isChanged() = runTest {
        val note = notes.random()
        assertFalse(noteUseCase.isChanged(note.id, note.title, note.text))
        assertTrue(noteUseCase.isChanged(note.id, "new title", "new text"))
    }

    @Test
    fun isEmpty() = runTest {
        val note = notes.random()
        assertFalse(noteUseCase.isEmpty(note.id))
    }

}