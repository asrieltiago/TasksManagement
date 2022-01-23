package com.asrieltiago.tasksmanagement.ui.task_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asrieltiago.tasksmanagement.data.model.Task
import com.asrieltiago.tasksmanagement.ui.theme.DarkBlue
import com.asrieltiago.tasksmanagement.ui.theme.LightBlue

@Composable
fun TaskItem(
    task: Task,
    onEvent: (TasksEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        onEvent(TasksEvent.OnDeleteTaskClick(task))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar",
                            tint = Color.Red
                        )
                    }
                }

                task.description?.let { description ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = description, style = MaterialTheme.typography.body2)
                }
            }

            Checkbox(checked = task.isDone, onCheckedChange = { isChecked ->
                onEvent(TasksEvent.OnDoneChange(task, isChecked))
            })
        }
    }
}