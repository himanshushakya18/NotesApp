package com.example.notesapp.persantation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notesapp.models.Notes
import com.example.notesapp.repository.StorageRepo
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.core.UserData.ParsedUpdateData
import com.google.firebase.ktx.Firebase

class DetailViewModel(
    private val repo: StorageRepo = StorageRepo()

) : ViewModel() {
    var detailUiState by mutableStateOf(DetailUiState())
        private set
    private val hasUser: Boolean
        get() = repo.hasUser
    private val user: FirebaseUser?
        get() = repo.user()

    fun onColorChange(color: Int) {
        detailUiState = detailUiState.copy(colorIndex = color)
    }

    fun onTitleChange(title: String) {
        detailUiState = detailUiState.copy(title = title)
    }

    fun onDescriptionChange(description: String) {
        detailUiState = detailUiState.copy(description = description)
    }

    fun addNote() {
        if (hasUser) {
            repo.addNote(
                userId = user!!.uid,
                title = detailUiState.title,
                description = detailUiState.description,
                color = detailUiState.colorIndex,
                timeStamp = Timestamp.now(),
            ) {
                detailUiState = detailUiState.copy(isNoteAdded = it)
            }
        }
    }

    fun setEditField(note: Notes) {
        detailUiState = detailUiState.copy(
            colorIndex = note.colorIndex,
            title = note.title,
            description = note.description,
        )

    }

    fun getNote(noteId: String) {
        repo.getNote(noteId, onError = {}) {
            detailUiState = detailUiState.copy(selectedNote = it)
            detailUiState.selectedNote?.let { it1 -> setEditField(it1) }

        }
    }
    fun updateNote(
        noteId: String
    ) {
        repo.updateNote(
            title = detailUiState.title,
            description = detailUiState.description,
            noteId = noteId,
            color = detailUiState.colorIndex,

            ) {
            detailUiState = detailUiState.copy(isNoteUpdateData = true)
        }
    }
    fun resetNoteStatus(){
        detailUiState = detailUiState.copy(isNoteUpdateData = false)
        detailUiState= detailUiState.copy(isNoteAdded = false   )
    }
    fun resetSate(){
        detailUiState = DetailUiState()
    }

}

data class DetailUiState(
    val colorIndex: Int = 0,
    val title: String = "",
    val description: String = "",
    val isNoteAdded: Boolean = false,
    val isNoteUpdateData: Boolean = false,
    val selectedNote: Notes? = null

)