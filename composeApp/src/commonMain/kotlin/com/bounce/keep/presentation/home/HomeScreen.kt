package com.bounce.keep.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.routes.Detail
import com.bounce.keep.presentation.routes.Editor
import com.bounce.keep.presentation.utils.UiState
import keepnotes.composeapp.generated.resources.Res
import keepnotes.composeapp.generated.resources.search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    topAppBarState: (TopAppBarState) -> Unit = {},
    navController: NavHostController? = null,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    modifier: Modifier = Modifier
) {
    val notepadUiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        topAppBarState(
            TopAppBarState(
                title = "Keep Notepad",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(Res.drawable.search),
                            contentDescription = null
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController?.navigate(Editor(0))
                    }) {
                        Text(text = "+", fontSize = 25.sp)
                    }
                }
            ))
    }

    when (val notepadData = notepadUiState.value) {
        UiState.Empty -> {}
        is UiState.Error -> {}
        UiState.Loading -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success<List<NotepadEntity>> -> {
            Column(modifier = modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notepadData.data.size) {
                        NoteItem(
                            notepadEntity = notepadData.data[it],
                            onClick = {
                                navController?.navigate(
                                    Detail(notepadData.data[it].id)
                                )
                            })
                    }
                }
            }
        }
    }


}

@Composable
@Preview(showBackground = true)
fun NoteItem(
    notepadEntity: NotepadEntity = NotepadEntity(
        title = "Plan for next month",
        notes = "Namangan 460 000 000 sum",
        date = "09 Nov 2025",
        color = 0xFFFBE4FF
    ),
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().requiredHeight(130.dp)
) {
    ElevatedCard(
        onClick = { onClick() }, colors = CardDefaults.elevatedCardColors(
            containerColor = Color(notepadEntity.color)
        ), modifier = modifier.padding(5.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            Text(
                text = notepadEntity.title,
                fontSize = 22.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = notepadEntity.notes,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = notepadEntity.date,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }

}