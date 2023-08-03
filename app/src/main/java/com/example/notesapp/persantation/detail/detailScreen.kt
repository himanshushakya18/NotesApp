@file:Suppress("UNUSED_EXPRESSION")

package com.example.notesapp.persantation.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapp.Utils
import com.example.notesapp.ui.theme.MyGreen
import com.example.notesapp.ui.theme.notSelected
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel?,
    noteId: String,
    onNavigation: () -> Unit
) {
    val detailUiState = viewModel?.detailUiState ?: DetailUiState()
    val isFormNotBlank = detailUiState.description.isNotBlank() && detailUiState.title.isNotBlank()
    val selectedColor by animateColorAsState(
        targetValue = Utils.colors[detailUiState.colorIndex],
        label = ""
    )
    val isNoteIdNotBlank = noteId.isNotBlank()
    val icon = if (isFormNotBlank) Icons.Default.Refresh else Icons.Default.Check
    LaunchedEffect(key1 = Unit) {
        if (isNoteIdNotBlank) {
            viewModel?.getNote(noteId)
        } else {
            viewModel?.resetSate()
        }
    }
    val scope = rememberCoroutineScope()
    val snakeBarHost = SnackbarHostState()
    Scaffold(
        snackbarHost = { snakeBarHost },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isNoteIdNotBlank) {
                        viewModel?.updateNote(noteId)
                        onNavigation()
                    } else {
                        viewModel?.addNote()
                        onNavigation()
                    }
                },
                containerColor = MyGreen
            ) {
                Icon(imageVector = icon, contentDescription = "add note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(selectedColor)
                .padding(paddingValues)
        ) {

            LaunchedEffect(key1 = detailUiState.isNoteAdded) {

                if (detailUiState.isNoteAdded) {
                    scope.launch {
                        snakeBarHost.showSnackbar(
                            "Note added successfully"
                        )
                    }

                }
            }
            LaunchedEffect(key1 = detailUiState.isNoteUpdateData) {

                if (detailUiState.isNoteUpdateData) {
                    scope.launch {

                        snakeBarHost.showSnackbar(
                            "Note updated successfully"
                        )
                    }
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                itemsIndexed(Utils.colors) { index, color ->
                    ColorItem(color = color, onClick ={
                        viewModel?.onColorChange(index)
                    } )
                }
            }
            OutlinedTextField(
                value = detailUiState.title,
                onValueChange = {
                    viewModel?.onTitleChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text(text = "Title") }
            )
            OutlinedTextField(
                value = detailUiState.description,
                onValueChange = {
                    viewModel?.onDescriptionChange(it)
                },
                label = { Text("Type your note here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            )


        }

    }
}

@Composable
fun ColorItem(color: Color, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = color,
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier
            .padding(8.dp)
            .size(40.dp)
            .clickable {
                onClick()
            }
    ) {

    }
}

@Preview
@Composable
fun Preview() {
    DetailScreen(viewModel = null, noteId = "") {

    }
}
