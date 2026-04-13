package com.bounce.keep.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.routes.Editor
import com.bounce.keep.presentation.utils.MyGlobalDialog
import com.bounce.keep.presentation.utils.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailScreen(
    topAppBarState: (TopAppBarState) -> Unit,
    navController: NavController,
    id: Int,
    viewModel: DetailViewModel = koinViewModel<DetailViewModel>(),
    modifier: Modifier = Modifier
) {
    val noteState by viewModel.noteState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        viewModel.getNoteById(id)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = noteState) {
            UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            UiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Note not found")
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }

            is UiState.Success -> {
                val note = state.data
                val backgroundColor = Color(note.color)

                LaunchedEffect(note, backgroundColor) {
                    topAppBarState(
                        TopAppBarState(
                            title = "",
                            navigationBack = {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    navController.navigate(Editor(note.id))
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            }
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (note.title.isNotEmpty()) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black.copy(alpha = 0.8f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = note.notes,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Last edited: ${note.date}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Black.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                MyGlobalDialog(
                    showDialog = showDeleteDialog,
                    onConfirm = {
                        showDeleteDialog = false
                        viewModel.deleteNoteById(note.id)
                        navController.navigateUp()
                    },
                    onDismiss = { showDeleteDialog = false },
                    title = "Delete Note",
                    message = "Are you sure you want to delete this note?",
                )
            }
        }
    }
}
