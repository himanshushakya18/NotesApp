package com.example.notesapp.persantation.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.models.Notes
import com.example.notesapp.repository.Resource
import com.example.notesapp.ui.theme.Background
import com.example.notesapp.ui.theme.MyGreen
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNoteClick: (id: String) -> Unit,
    navigateToDetailScreen: () -> Unit,
    navigateToSignIn: () -> Unit,
    viewModel: HomeViewModel?,

    ) {
    val homeUiState = viewModel?.homeUiState
    var selectedNote: Notes? by remember {
        mutableStateOf(null)
    }
    var openDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        viewModel?.loadNote()
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_exit),
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            viewModel?.signOut()
                            navigateToSignIn()
                        }
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MyGreen,
                    titleContentColor = Color.White
                ),
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToDetailScreen() },
                containerColor = MyGreen
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    tint = Color.White
                )
            }

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)

        ) {
            when (homeUiState?.noteList) {
                is Resource.Loading -> {

                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        CircularProgressIndicator(
                            color  = MyGreen
                        )
                    }
                }

                is Resource.Success -> {
                    val list = homeUiState.noteList.data
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(list ?: emptyList()) { note ->
                            ListItem(
                                note = note,
                                color = Utils.colors[note.colorIndex],
                                onLongClick = {
                                    selectedNote = note
                                    openDialog = true
                                },
                                onClick = {
                                    onNoteClick(note.noteId)
                                    selectedNote = note
                                }
                            )
                        }
                    }
                    AnimatedVisibility(visible = openDialog) {
                        AlertDialog(
                            onDismissRequest = { openDialog = false },
                            title ={Text("Delete note?")},
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedNote?.let { viewModel?.deleteNote(noteId = it.noteId) }
                                        openDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Delete")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { openDialog = false    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MyGreen,
                                        contentColor = Color.White
                                    )
                                ) {
                                        Text("Cancel")
                                }
                            }
                        )
                    }

                }

                else -> {

                    Log.d(
                        "someError",
                        " ${homeUiState?.noteList?.throwable?.localizedMessage ?: "Unknown Error"}"
                    )

                }
            }
        }

    }

    LaunchedEffect(key1 = viewModel?.hasUser) {

        if (viewModel?.hasUser == false) {
            navigateToSignIn()
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(note: Notes, color: Color, onClick: () -> Unit, onLongClick: () -> Unit) {


    Column(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp)
            .background(color, RoundedCornerShape(16.dp))
            .size(200.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                RoundedCornerShape(16.dp)
            ),
    ) {


        Text(
            text = note.title,
            modifier = Modifier.padding(4.dp),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = note.description,
            modifier = Modifier.padding(4.dp),
            maxLines = 3,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))

            Text(
                text = formatDate(note.timestamp),
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.End),

                style = MaterialTheme.typography.bodyMedium
            )

    }


}

private fun formatDate(timestamp: com.google.firebase.Timestamp): String {
    val formattedDate = SimpleDateFormat("dd-MM-yy hh:mm", Locale.getDefault())
    return formattedDate.format(timestamp.toDate())
}
