package com.asrieltiago.tasksmanagement.ui.task_list

import com.asrieltiago.tasksmanagement.data.model.Task

sealed class TasksEvent {

    data class OnDeleteTaskClick(val task: Task) : TasksEvent()
    data class OnDoneChange(val task: Task, val isDone: Boolean) : TasksEvent()
    data class OnTaskClick(val task: Task) : TasksEvent()
    object OnUndoDeleteClick : TasksEvent()
    object OnAddTaskClick : TasksEvent()
}
