package com.asrieltiago.tasksmanagement.ui.add_edit_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asrieltiago.tasksmanagement.data.local.TaskRepository
import com.asrieltiago.tasksmanagement.data.model.Task
import com.asrieltiago.tasksmanagement.util.SnackbarType
import com.asrieltiago.tasksmanagement.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var task by mutableStateOf<Task?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val taskId = savedStateHandle.get<Int>("taskId")
        if (isEditing(taskId)) {
            viewModelScope.launch {
                sendUiEvent(UiEvent.IsEditing)

                repository.getTaskById(taskId!!)?.let { task ->
                    title = task.title
                    description = task.description ?: ""

                    this@AddEditTaskViewModel.task = task
                }
            }
        }
    }

    private fun isEditing(taskId: Int?) = taskId != -1 && taskId != null

    fun onEvent(event: AddEditTaskEvent) {
        when (event) {
            is AddEditTaskEvent.OnDescriptionChange -> description = event.description
            is AddEditTaskEvent.OnBackClick -> viewModelScope.launch {
                sendUiEvent(UiEvent.PopBackstack)
            }
            is AddEditTaskEvent.OnSaveTaskClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                type = SnackbarType.Error
                            )
                        )
                        return@launch
                    }
                    repository.insertTask(
                        Task(
                            title = title,
                            description = description,
                            isDone = task?.isDone ?: false,
                            id = task?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackstack)
                }
            }
            is AddEditTaskEvent.OnTitleChange -> title = event.title
        }
    }

    private suspend fun sendUiEvent(event: UiEvent) {
        _uiEvent.send(event)
    }
}