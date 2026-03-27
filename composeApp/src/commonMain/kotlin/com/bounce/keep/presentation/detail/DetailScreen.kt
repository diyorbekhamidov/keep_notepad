package com.bounce.keep.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.routes.Editor
import com.bounce.keep.presentation.utils.MyGlobalDialog
import com.bounce.keep.presentation.utils.UiState
import keepnotes.composeapp.generated.resources.Res
import keepnotes.composeapp.generated.resources.delete
import keepnotes.composeapp.generated.resources.edit
import keepnotes.composeapp.generated.resources.nav_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview(showBackground = true)
fun DetailScreen(
    topAppBarState: (TopAppBarState) -> Unit,
    navController: NavController,
    id: Int,
    viewModel: DetailViewModel = koinInject<DetailViewModel>(),
    modifier: Modifier = Modifier
) {

    SideEffect {
        viewModel.getNoteById(id)
    }

    val noteData = viewModel.note.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    when (val data = noteData.value) {
        UiState.Empty -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Not found!", modifier = Modifier.align(Alignment.Center))
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = data.message, modifier = Modifier.align(Alignment.Center))
            }
        }

        UiState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is UiState.Success<NotepadEntity?> -> {
            LaunchedEffect(true) {
                topAppBarState(
                    TopAppBarState(
                        title = "Detail",
                        navigationBack = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.nav_back),
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigateUp()
                                navController.navigate(
                                    Editor(
                                        data.data?.id ?: 0
                                    )
                                )
                            }) {
                                Icon(
                                    painter = painterResource(Res.drawable.edit),
                                    contentDescription = null
                                )
                            }

                            IconButton(onClick = { showDialog = true }) {
                                Icon(
                                    painter = painterResource(Res.drawable.delete),
                                    contentDescription = null
                                )
                            }
                        }
                    ))
            }

            Column(
                modifier = modifier.background(color = Color(data.data?.color ?: 0)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = data.data?.title ?: "",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider()
                Text(
                    text = data.data?.notes ?: "",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                )
            }

            MyGlobalDialog(
                showDialog = showDialog,
                onConfirm = {
                    showDialog = false
                    viewModel.deleteNoteById(id)
                    navController.navigateUp()
                },
                onDismiss = { showDialog = false },
                title = "Delete this note",
                message = "Do you want to delete this note?",
            )

        }
    }
}