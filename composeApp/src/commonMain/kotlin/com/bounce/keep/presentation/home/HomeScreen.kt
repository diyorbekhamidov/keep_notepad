package com.bounce.keep.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.routes.Detail
import com.bounce.keep.presentation.routes.Editor
import com.bounce.keep.presentation.utils.UiState
import keepnotes.composeapp.generated.resources.Res
import keepnotes.composeapp.generated.resources.add
import keepnotes.composeapp.generated.resources.close
import keepnotes.composeapp.generated.resources.search
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    topAppBarState: (TopAppBarState) -> Unit = {},
    navController: NavHostController? = null,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        when (val state = uiState) {
            UiState.Empty -> {
                DisposableEffect(isSearchActive) {
                    topAppBarState(
                        TopAppBarState(
                            title = "Keep Notepad",
                            actions = {
                                SearchActions(
                                    isSearchActive = isSearchActive,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                                    onSearchToggle = { isSearchActive = true },
                                    onSearchClose = {
                                        isSearchActive = false
                                        viewModel.onSearchQueryChange("")
                                    }
                                )
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { navController?.navigate(Editor(0)) },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.add),
                                        contentDescription = "Add Note"
                                    )
                                }
                            }
                        )
                    )
                    onDispose { }
                }
                EmptyState(isSearchActive)
            }

            is UiState.Error -> {
                DisposableEffect(Unit) {
                    topAppBarState(
                        TopAppBarState(title = "Keep Notepad")
                    )
                    onDispose { }
                }
                ErrorState(state.message)
            }

            UiState.Loading -> {
                DisposableEffect(Unit) {
                    topAppBarState(
                        TopAppBarState(title = "Keep Notepad")
                    )
                    onDispose { }
                }
                LoadingState()
            }

            is UiState.Success -> {
                DisposableEffect(isSearchActive) {
                    topAppBarState(
                        TopAppBarState(
                            title = if (isSearchActive) "" else "Keep Notepad",
                            actions = {
                                SearchActions(
                                    isSearchActive = isSearchActive,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                                    onSearchToggle = { isSearchActive = true },
                                    onSearchClose = {
                                        isSearchActive = false
                                        viewModel.onSearchQueryChange("")
                                    }
                                )
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { navController?.navigate(Editor(0)) },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.add),
                                        contentDescription = "Add Note"
                                    )
                                }
                            }
                        )
                    )
                    onDispose { }
                }
                NotesGrid(
                    notes = state.data,
                    onNoteClick = { note ->
                        navController?.navigate(Detail(note.id))
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.SearchActions(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onSearchClose: () -> Unit
) {
    if (isSearchActive) {
        val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    "Search notes...",
                    color = contentColor.copy(alpha = 0.7f)
                )
            },
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = contentColor,
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = onSearchClose) {
                    Icon(
                        painter = painterResource(Res.drawable.close),
                        contentDescription = "Close Search"
                    )
                }
            }
        )
    } else {
        IconButton(onClick = onSearchToggle) {
            Icon(
                painter = painterResource(Res.drawable.search),
                contentDescription = "Search"
            )
        }
    }
}

@Composable
fun NotesGrid(
    notes: List<NotepadEntity>,
    onNoteClick: (NotepadEntity) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(notes, key = { it.id }) { note ->
            NoteItem(note = note, onClick = { onNoteClick(note) })
        }
    }
}

@Composable
fun NoteItem(
    note: NotepadEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(note.color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (note.title.isNotEmpty()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.8f)
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = note.notes,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black.copy(alpha = 0.7f)
                ),
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = note.date,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun EmptyState(isSearch: Boolean) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isSearch) "No notes match your search" else "No notes yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}
