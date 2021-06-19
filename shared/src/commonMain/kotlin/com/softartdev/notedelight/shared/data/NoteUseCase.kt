package com.softartdev.notedelight.shared.data

import com.softartdev.notedelight.shared.database.DatabaseRepo
import com.softartdev.notedelight.shared.date.createLocalDateTime
import com.softartdev.notedelight.shared.db.Note
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class NoteUseCase(
        private val dbRepo: DatabaseRepo
) {
    val titleChannel: Channel<String> by lazy { return@lazy Channel<String>() }

    fun doOnRelaunchFlow(function: (() -> Unit)?) {
        dbRepo.relaunchFlowEmitter = function
    }

    fun getNotes(): Flow<List<Note>> = TODO("remove or change on Realm")

    fun launchNotes(
        onSuccess: (List<Note>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) = try {
        val query: List<Note> = TODO("remove or change on Realm")
        onSuccess(query)
    } catch (t: Throwable) {
        t.printStackTrace()
        onFailure(t)
    }

    @Throws(Exception::class) suspend fun createNote(title: String = "", text: String = ""): Long {
        val notes: List<Note> = TODO("remove or change on Realm")
        val noteId = if (notes.isEmpty()) 1 else notes.last().id + 1
        val localDateTime = createLocalDateTime()
        val note = Note(noteId, title, text, localDateTime, localDateTime)
//        dbRepo.noteQueries.insert(note)//TODO remove or change on Realm
        return noteId
    }

    @Throws(Exception::class) suspend fun saveNote(id: Long, title: String, text: String): Note {
        val note = loadNote(id).copy(
                title = title,
                text = text,
                dateModified = createLocalDateTime()
        )
//        dbRepo.noteQueries.update(note)//TODO remove or change on Realm
        return note
    }

    suspend fun updateTitle(id: Long, title: String): Int {
        val note = loadNote(id).copy(
                title = title,
                dateModified = createLocalDateTime()
        )
//        dbRepo.noteQueries.update(note)//TODO remove or change on Realm
        return 1
    }

    @Throws(Exception::class) suspend fun loadNote(noteId: Long): Note = TODO("remove or change on Realm")

    suspend fun deleteNote(id: Long): Int = TODO("remove or change on Realm")

    @Throws(Exception::class) suspend fun deleteNoteUnit(id: Long): Unit = TODO("remove or change on Realm")

    suspend fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = loadNote(id)
        return note.title != title || note.text != text
    }

    suspend fun isEmpty(id: Long): Boolean {
        val note = loadNote(id)
        return note.title.isEmpty() && note.text.isEmpty()
    }

}