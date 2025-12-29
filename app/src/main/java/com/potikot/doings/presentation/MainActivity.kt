package com.potikot.doings.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.potikot.doings.presentation.main.MainScreen
import com.potikot.doings.presentation.project.ProjectScreen
import com.potikot.doings.presentation.util.Screen
import com.potikot.doings.ui.theme.DoingsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoingsTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Main.route
                    ) {
                        composable(
                            route = Screen.Main.route
                        ) {
                            MainScreen(navController)
                        }
                        composable(
                            route = Screen.Project.route + "/{projectId}",
                            arguments = listOf(navArgument("projectId") {
                                type = NavType.LongType
                            })
                        ) {
                            ProjectScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}