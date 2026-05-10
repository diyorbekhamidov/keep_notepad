package com.bounce.keep.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.home.EmptyState
import com.bounce.keep.presentation.home.ErrorState
import com.bounce.keep.presentation.home.LoadingState
import com.bounce.keep.presentation.routes.Editor
import com.bounce.keep.presentation.utils.MyGlobalDialog
import com.bounce.keep.presentation.utils.UiState
import keepnotes.composeapp.generated.resources.Res
import keepnotes.composeapp.generated.resources.delete
import keepnotes.composeapp.generated.resources.edit
import keepnotes.composeapp.generated.resources.nav_back
import org.jetbrains.compose.resources.painterResource
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

    DisposableEffect(id) {
        viewModel.getNoteById(id)
        onDispose { }
    }

    when (val state = noteState) {
        UiState.Loading -> {
            DisposableEffect(Unit) {
                topAppBarState(
                    TopAppBarState(
                        title = "Loading...",
                        navigationBack = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.nav_back),
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                )
                onDispose { }
            }
            LoadingState()
        }

        UiState.Empty -> {
            DisposableEffect(Unit) {
                topAppBarState(
                    TopAppBarState(
                        title = "Note",
                        navigationBack = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.nav_back),
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                )
                onDispose { }
            }
            EmptyState(isSearch = false)
        }

        is UiState.Error -> {
            DisposableEffect(Unit) {
                topAppBarState(
                    TopAppBarState(
                        title = "Error",
                        navigationBack = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.nav_back),
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                )
                onDispose { }
            }
            ErrorState(state.message)
        }

        is UiState.Success -> {
            val note = state.data
            val backgroundColor = Color(note.color)

            DisposableEffect(Unit) {
                topAppBarState(
                    TopAppBarState(
                        title = "Note",
                        navigationBack = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.nav_back),
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(Editor(note.id))
                            }) {
                                Icon(
                                    painter = painterResource(Res.drawable.edit),
                                    contentDescription = "Edit"
                                )
                            }
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(
                                    painter = painterResource(Res.drawable.delete),
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    )
                )
                onDispose { }
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
