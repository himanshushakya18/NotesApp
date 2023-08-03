package com.example.notesapp.persantation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesapp.models.Notes
import com.example.notesapp.repository.Resource
import com.example.notesapp.repository.StorageRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: StorageRepo = StorageRepo()
) : ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
    val user = repo.user()
    val hasUser: Boolean
        get() = repo.hasUser
    private val userId: String
        get() = repo.getUserId()

    fun loadNote() {
        if (hasUser) {
            if (userId.isNotBlank()) {

                getUserNote(userId)
            } else {
                homeUiState =
                    homeUiState.copy(noteList = Resource.Error(throwable = Throwable(message = "user not found")))
            }

        }
    }
    private fun getUserNote(userId: String) = viewModelScope.launch {
        repo.getUserNotes(userId).collect() {
            homeUiState = homeUiState.copy(
                noteList = it
            )
        }
    }
    fun deleteNote(noteId:String)=repo.deleteNote(noteId){
        homeUiState = homeUiState.copy(deletedStatus = it)
    }
    fun signOut()= repo.signOut()
}


data class HomeUiState(
    var noteList: Resource<List<Notes>> = Resource.Loading(),
    var deletedStatus: Boolean = false
)