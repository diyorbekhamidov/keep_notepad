package com.bounce.keep.presentation.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.utils.UiState
import keepnotes.composeapp.generated.resources.Res
import keepnotes.composeapp.generated.resources.nav_back
import keepnotes.composeapp.generated.resources.save
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview(showBackground = true)
fun EditorScreen(
    topAppBarState: (TopAppBarState) -> Unit,
    navController: NavHostController,
    id: Int,
    viewModel: EditorViewModel = koinViewModel<EditorViewModel>(),
    modifier: Modifier = Modifier
) {

    SideEffect {
        if (id > 0)
            viewModel.getNotepadById(id)
        else
            viewModel.resetState()
    }

    val notepad = viewModel.notepad.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    when (val notepadData = notepad.value) {
        UiState.Empty -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = "Not found note!")
            }
        }

        is UiState.Error -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = notepadData.message)
            }
        }

        UiState.Loading -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success<NotepadEntity> -> {

            var title by rememberSaveable { mutableStateOf(notepadData.data.title) }
            var notes by rememberSaveable { mutableStateOf(notepadData.data.notes) }

            LaunchedEffect(true) {
                topAppBarState(
                    TopAppBarState(
                        navigationBack = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.nav_back),
                                    contentDescription = null
                                )
                            }
                        },
                        title = "Editor",
                        actions = {
                            IconButton(onClick = {
                                if (title.length > 3 && notes.length > 3) {
                                    if (id > 0) viewModel.updateNote(
                                        id,
                                        title,
                                        notes,
                                        notepadData.data.color
                                    )
                                    else
                                        viewModel.insertNote(title, notes)
                                    navController.navigateUp()
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "You must minimum 3 characters to save note!",
                                            actionLabel = null,
                                            withDismissAction = true,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }) {
                                Icon(
                                    painter = painterResource(Res.drawable.save),
                                    contentDescription = null
                                )
                            }
                        }
                    ))
            }

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)) {

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Title", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Type something...", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}