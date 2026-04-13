package com.bounce.keep.presentation.editor

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.utils.UiState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditorScreen(
    topAppBarState: (TopAppBarState) -> Unit,
    navController: NavHostController,
    id: Int,
    viewModel: EditorViewModel = koinViewModel<EditorViewModel>(),
    modifier: Modifier = Modifier
) {
    val noteState by viewModel.noteState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

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
                EditorContent(
                    note = state.data,
                    topAppBarState = topAppBarState,
                    navController = navController,
                    onSave = { title, notes ->
                        viewModel.upsertNote(id, title, notes, state.data.color)
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
fun EditorContent(
    note: NotepadEntity,
    topAppBarState: (TopAppBarState) -> Unit,
    navController: NavHostController,
    onSave: (String, String) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note.title) }
    var notes by rememberSaveable { mutableStateOf(note.notes) }
    val backgroundColor = Color(note.color)

    LaunchedEffect(title, notes, backgroundColor) {
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
                        if (title.isNotBlank() || notes.isNotBlank()) {
                            onSave(title, notes)
                        } else {
                            navController.navigateUp()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
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
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = {
                Text(
                    "Title",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.4f)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = {
                Text(
                    "Note",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black.copy(alpha = 0.4f)
                    )
                )
            },
            modifier = Modifier.fillMaxSize(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Black.copy(alpha = 0.7f)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
