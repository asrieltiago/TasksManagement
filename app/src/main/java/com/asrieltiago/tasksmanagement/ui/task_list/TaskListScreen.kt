package com.asrieltiago.tasksmanagement.ui.task_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asrieltiago.tasksmanagement.ui.theme.LightBlue
import com.asrieltiago.tasksmanagement.util.UiEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@OptIn(InternalCoroutinesApi::class)
@Composable
fun TaskListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TasksEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TasksEvent.OnAddTaskClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar"
                )
            }
        }
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Suas Tarefas Diárias", style = MaterialTheme.typography.h1, color = LightBlue)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tasks.value) { task ->
                    TaskItem(
                        task = task,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(TasksEvent.OnTaskClick(task))
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
