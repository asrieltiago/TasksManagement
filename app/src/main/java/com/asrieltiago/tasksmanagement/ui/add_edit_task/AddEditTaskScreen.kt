package com.asrieltiago.tasksmanagement.ui.add_edit_task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asrieltiago.tasksmanagement.R
import com.asrieltiago.tasksmanagement.ui.theme.LightBlue
import com.asrieltiago.tasksmanagement.util.SnackbarType
import com.asrieltiago.tasksmanagement.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun AddEditTaskScreen(
    onPopBackstack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var screenTitle by remember {
        mutableStateOf(context.getString(R.string.create_new_task))
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackstack -> {
                    onPopBackstack()
                }
                is UiEvent.ShowSnackbar -> {
                    if (event.type == SnackbarType.Error) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_empty_title)
                        )
                    }
                }
                is UiEvent.IsEditing -> screenTitle = context.getString(R.string.edit_task)
                else -> Unit
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTaskEvent.OnSaveTaskClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = context.getString(R.string.save)
                )
            }
        },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { viewModel.onEvent(AddEditTaskEvent.OnBackClick) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colors.primary
                    )
                }
                Text(
                    text = screenTitle,
                    style = MaterialTheme.typography.h1,
                    color = LightBlue
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.OnTitleChange(it)) },
                label = {
                    Text(text = stringResource(R.string.title))
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
                    Text(text = stringResource(R.string.description))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                singleLine = false,
                maxLines = 5,
                textStyle = MaterialTheme.typography.body2
            )
        }
    }
}