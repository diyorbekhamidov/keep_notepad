package com.bounce.keep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bounce.keep.presentation.TopAppBarState
import com.bounce.keep.presentation.detail.DetailScreen
import com.bounce.keep.presentation.editor.EditorScreen
import com.bounce.keep.presentation.home.HomeScreen
import com.bounce.keep.presentation.routes.Detail
import com.bounce.keep.presentation.routes.Editor
import com.bounce.keep.presentation.routes.Home
import com.bounce.keep.presentation.ui.theme.Blue80
import com.bounce.keep.presentation.ui.theme.KeepNotesTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {

    val navController = rememberNavController()
    var topAppBarState by remember { mutableStateOf(TopAppBarState()) }


    KeepNotesTheme {

        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = topAppBarState.title)
                }, actions = {
                    topAppBarState.actions?.invoke(this)
                }, navigationIcon = {
                    topAppBarState.navigationBack?.invoke()
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue80,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }, floatingActionButton = {
            topAppBarState.floatingActionButton?.invoke()
        }) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Home,
                modifier = Modifier.padding(innerPadding)
            ) {

                composable<Home> {
                    HomeScreen(
                        topAppBarState = {
                            topAppBarState = it
                        },
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable<Editor> { backStackEntry ->
                    val editor = backStackEntry.toRoute<Editor>()

                    EditorScreen(
                        topAppBarState = {
                            topAppBarState = it
                        },
                        id = editor.id,
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable<Detail> { backStackEntry ->
                    val detail = backStackEntry.toRoute<Detail>()

                    DetailScreen(
                        topAppBarState = {
                            topAppBarState = it
                        },
                        navController = navController,
                        id = detail.id,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

    }
}