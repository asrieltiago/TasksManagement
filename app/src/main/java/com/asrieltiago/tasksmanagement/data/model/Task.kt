package com.asrieltiago.tasksmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null
)
