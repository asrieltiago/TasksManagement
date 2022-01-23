package com.asrieltiago.tasksmanagement.ui.add_edit_task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asrieltiago.tasksmanagement.ui.theme.LightBlue
import com.asrieltiago.tasksmanagement.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun AddEditTaskScreen(
    onPopBackstack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    var screenTitle by remember {
        mutableStateOf("Criar Nova Tarefa")
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackstack ->  {
                    onPopBackstack()
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                is UiEvent.IsEditing -> screenTitle = "Editar Tarefa"
                else -> Unit
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState, modifier = Modifier
        .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTaskEvent.OnSaveTaskClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Salvar"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = screenTitle,
                style = MaterialTheme.typography.h1,
                color = LightBlue
            )

            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.OnTitleChange(it)) },
                label = {
                    Text(text = "Título")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.OnDescriptionChange(it)) },
                label = {
                    Text(text = "Descrição")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = false,
                maxLines = 5,
                textStyle = MaterialTheme.typography.body2
            )
        }
    }
}