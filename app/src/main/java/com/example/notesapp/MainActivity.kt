package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesapp.persantation.Login.LogInViewModel
import com.example.notesapp.persantation.detail.DetailViewModel
import com.example.notesapp.persantation.home.HomeScreen
import com.example.notesapp.persantation.home.HomeViewModel
import com.example.notesapp.repository.AuthRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authRepo = AuthRepository(this)
            val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
            val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
            val logInViewModel = viewModel<LogInViewModel>(
                factory = object: ViewModelProvider.Factory{
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LogInViewModel(authRepo) as T
                    }
                }
            )
            Navigation(logInViewModel = logInViewModel, homeViewModel =homeViewModel , detailViewModel = detailViewModel)
        }
    }
}

