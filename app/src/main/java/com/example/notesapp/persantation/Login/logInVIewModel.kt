package com.example.notesapp.persantation.Login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.repository.AuthRepository
import kotlinx.coroutines.launch

class LogInViewModel(
    private val repo: AuthRepository,
) : ViewModel() {
    val currentUser = repo.currentUser
    val hasUser: Boolean
        get() = repo.hasUser()
    var logInUiState by mutableStateOf(LogInUiState())
        private set

    fun onUserNameChange(userName: String) {
        logInUiState = logInUiState.copy(
            userName = userName
        )
    }

    fun onPasswordChange(password: String) {
        logInUiState = logInUiState.copy(password = password)
    }

    fun onSignUpUserNameChange(userName: String) {
        logInUiState = logInUiState.copy(userNameSignup = userName)
    }

    fun onSignUpPasswordChange(password: String) {
        logInUiState = logInUiState.copy(passwordSignup = password)
    }

    fun onSignUpConfirmPasswordChange(password: String) {
        logInUiState = logInUiState.copy(confirmPasswordSignup = password)
    }

    fun validateLoginForm() =
        logInUiState.userName.isNotBlank() &&
                logInUiState.password.isNotBlank()

    fun validateSignUpForm() =
        logInUiState.userNameSignup.isNotBlank() &&
                logInUiState.passwordSignup.isNotBlank() &&
                logInUiState.confirmPasswordSignup.isNotBlank()


    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignUpForm()) {
                throw IllegalArgumentException("email and password can't be empty")
            }
            logInUiState = logInUiState.copy(isLoading = true)
            if (logInUiState.passwordSignup != logInUiState.confirmPasswordSignup) {
                throw IllegalArgumentException("password do not match")
            }
            logInUiState = logInUiState.copy(signUpError = null)
            repo.createUser(logInUiState.userNameSignup, logInUiState.passwordSignup) {
                if (it) {
                    Toast.makeText(context, "success login", Toast.LENGTH_LONG).show()
                    logInUiState = logInUiState.copy(isSuccessLogIn = true)
                } else {
                    Toast.makeText(context, "Failed login", Toast.LENGTH_LONG).show()
                    logInUiState = logInUiState.copy(isSuccessLogIn = false)
                }

            }
        } catch (e: Exception) {
            logInUiState = logInUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            logInUiState = logInUiState.copy(isLoading = false)
        }
    }

    fun logIn(context: Context) = viewModelScope.launch {
        try {
            if (!validateLoginForm()) {
                throw IllegalArgumentException("email and password can't be empty")
            }
            logInUiState = logInUiState.copy(isLoading = true)
            logInUiState = logInUiState.copy(logInError = null)
            repo.signIn(logInUiState.userName, logInUiState.password) {
                if (it) {
                    Toast.makeText(context, "success login", Toast.LENGTH_LONG).show()
                    logInUiState = logInUiState.copy(isSuccessLogIn = true)
                } else {
                    Toast.makeText(context, "Failed login", Toast.LENGTH_LONG).show()
                    logInUiState = logInUiState.copy(isSuccessLogIn = false)
                }

            }
        } catch (e: Exception) {
            logInUiState = logInUiState.copy(logInError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            logInUiState = logInUiState.copy(isLoading = false)
        }
    }

    fun signOut() {
        repo.signOut()

    }
}


data class LogInUiState(
    var userName: String = "",
    var password: String = "",
    var userNameSignup: String = "",
    var passwordSignup: String = "",
    var confirmPasswordSignup: String = "",
    var isLoading: Boolean = false,
    var isSuccessLogIn: Boolean = false,
    var signUpError: String? = "",
    var logInError: String? = ""
)