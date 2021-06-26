package com.softartdev.notedelight.shared.data

import com.softartdev.notedelight.shared.database.DatabaseRepo
import com.softartdev.notedelight.shared.db.Note
import io.realm.Cancellable
import io.realm.RealmResults
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class NoteUseCase(
        private val dbRepo: DatabaseRepo
) {
    private val realm by lazy { dbRepo.realm }

    val titleChannel: Channel<String> by lazy { return@lazy Channel<String>() }

    fun doOnRelaunchFlow(function: (() -> Unit)?) {
        dbRepo.relaunchFlowEmitter = function
    }

    fun getNotes(): Flow<List<Note>> = callbackFlow {
        val cancellable: Cancellable = realm.objects<Note>().observe { result: RealmResults<Note> ->
            trySend(result.toList()).isSuccess // FIXME RealmResults is the same (equals) causing the compose to not re-compose (maybe define a hashcode/equals based on size or Core version/counter of the list)
        }
        awaitClose(cancellable::cancel)
    }

    fun launchNotes(
        onSuccess: (List<Note>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) = try {
        realm.objects<Note>().observe { result: RealmResults<Note> ->
            onSuccess(result.toList()) // FIXME RealmResults is the same (equals) causing the compose to not re-compose (maybe define a hashcode/equals based on size or Core version/counter of the list)
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        onFailure(t)
    }

    @Throws(Exception::class) suspend fun createNote(title: String = "", text: String = ""): Long {
        return realm.writeBlocking {
            val note = create<Note>()
            note.title = title
            note.text = text
            return@writeBlocking note.id
        }
    }

    @Throws(Exception::class) suspend fun saveNote(id: Long, title: String, text: String): Note {
        return realm.writeBlocking {
            val note = objects(Note::class).query("id = $0", id).first()
            note.title = title
            note.text = text
            return@writeBlocking note
        }
    }

    suspend fun updateTitle(id: Long, title: String): Int {
        realm.writeBlocking {
            val note = objects(Note::class).query("id = $0", id).first()
            note.title = title
        }
        return 1
    }

    @Throws(Exception::class) suspend fun loadNote(noteId: Long): Note {
        return realm.objects(Note::class).query("id = $0", noteId).first()
    }

    suspend fun deleteNote(id: Long): Int = realm.writeBlocking {
        return@writeBlocking objects(Note::class)
            .query("id = $0", id)
            .onEach(::delete)
            .size
    }

    @Throws(Exception::class) suspend fun deleteNoteUnit(id: Long): Unit = realm.writeBlocking {
        return@writeBlocking objects(Note::class)
            .query("id = $0", id)
            .forEach(::delete)
    }

    suspend fun isChanged(id: Long, title: String, text: String): Boolean {
        val note = loadNote(id)
        return note.title != title || note.text != text
    }

    suspend fun isEmpty(id: Long): Boolean {
        val note = loadNote(id)
        return note.title.isEmpty() && note.text.isEmpty()
    }

}