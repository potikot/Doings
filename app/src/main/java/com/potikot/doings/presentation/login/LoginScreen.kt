package com.potikot.doings.presentation.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potikot.doings.presentation.components.ListPanel
import com.potikot.doings.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var login by remember { mutableStateOf("ildus321123@gmail.com") }
    var password by remember { mutableStateOf("9!KuB4(_)pS6@AYCPkc") }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.list == null) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = login,
                            placeholder = { Text(text = "Логин...", color = MaterialTheme.colorScheme.onSurface.copy(0.5f)) },
                            onValueChange = { login = it },
                            modifier = Modifier.fillMaxWidth()
                        )

                        TextField(
                            value = password,
                            placeholder = { Text(text = "Пароль...", color = MaterialTheme.colorScheme.onSurface.copy(0.5f)) },
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            enabled = !state.isHandlingRequest,
                            onClick = { viewModel.sendEvent(LoginEvent.ListCompanies(login, password)) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Войти")
                        }
                    }
                }
            } else {
                ListPanel(
                    list = state.list!!,
                    lastElement = state.lastElement,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp),
                    createElement = { e, modifier ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 8.dp, vertical = 12.dp)
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        e.onClick()
                                        state.keys?.let { navController.navigate(Screen.Main.route) }
                                    },
                                )
//                                .combinedClickable(
//                                    onClick = {
//                                        e.onClick()
//                                        state.keys?.let { navController.navigate(Screen.Main.route) }
//                                    },
//                                    onLongClick = e.onLongClick
//                                )
                        ) {
                            e.leadingIcon?.let {
                                Icon(
                                    imageVector = e.leadingIcon,
                                    contentDescription = null,
                                )
                            }
                            Text(
                                e.label,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                )
            }
        }
    }
}