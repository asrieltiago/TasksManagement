package com.asrieltiago.tasksmanagement.ui.task_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asrieltiago.tasksmanagement.R
import com.asrieltiago.tasksmanagement.ui.theme.LightBlue
import com.asrieltiago.tasksmanagement.util.SnackbarType
import com.asrieltiago.tasksmanagement.util.UiEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@OptIn(InternalCoroutinesApi::class)
@Composable
fun TaskListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tasks = viewModel.tasks.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    if (event.type == SnackbarType.Delete) {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = context.getString(R.string.task_deleted),
                            actionLabel = context.getString(R.string.undo)
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(TasksEvent.OnUndoDeleteClick)
                        }
                    }
                }
                is UiEvent.Navigate -> {
                    dismissSnackbar(scaffoldState)
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
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        topBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp),
                text = stringResource(R.string.your_daily_tasks),
                style = MaterialTheme.typography.h1,
                color = LightBlue
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (tasks.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.empty_tasks_message),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tasks.value) { task ->
                        AnimatedVisibility(
                            visible = !task.isVisible,
                            exit = fadeOut(
                                animationSpec = TweenSpec(200, 200, FastOutLinearInEasing)
                            )
                        ) {
                            TaskItem(
                                task = task,
                                onEvent = viewModel::onEvent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        dismissSnackbar(scaffoldState)
                                        viewModel.onEvent(TasksEvent.OnTaskClick(task))
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun dismissSnackbar(scaffoldState: ScaffoldState) {
    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
}
