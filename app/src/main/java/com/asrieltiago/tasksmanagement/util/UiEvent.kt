package com.asrieltiago.tasksmanagement.util

sealed class UiEvent {
    object IsEditing : UiEvent()
    object PopBackstack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ) : UiEvent()
}