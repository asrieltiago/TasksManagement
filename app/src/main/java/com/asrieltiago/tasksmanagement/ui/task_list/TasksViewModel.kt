package com.asrieltiago.tasksmanagement.ui.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asrieltiago.tasksmanagement.data.local.TaskRepository
import com.asrieltiago.tasksmanagement.data.model.Task
import com.asrieltiago.tasksmanagement.util.Routes
import com.asrieltiago.tasksmanagement.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.getTasks()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTask: Task? = null

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.OnTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK + "?taskId=${event.task.id}"))
            }
            is TasksEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTask(
                        event.task.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
            is TasksEvent.OnDeleteTaskClick -> {
                viewModelScope.launch {
                    deletedTask = event.task
                    repository.deleteTask(event.task)

                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "Tarefa deletada",
                            action = "Desfazer"
                        )
                    )
                }
            }
            is TasksEvent.OnUndoDeleteClick -> {
                deletedTask?.let { task ->
                    viewModelScope.launch {
                        repository.insertTask(task)
                    }
                }
            }
            is TasksEvent.OnAddTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}