package com.asrieltiago.tasksmanagement.util

sealed class SnackbarType {
    object Delete : SnackbarType()
    object Error : SnackbarType()
}