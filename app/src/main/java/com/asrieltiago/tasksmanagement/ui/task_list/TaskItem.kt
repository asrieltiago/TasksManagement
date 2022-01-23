package com.asrieltiago.tasksmanagement.ui.task_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.asrieltiago.tasksmanagement.data.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskItem(
    task: Task,
    onEvent: (TasksEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = (-68).dp
    var swipeableState = rememberSwipeableState(0)
    val iconPx = with(LocalDensity.current) { iconSize.toPx() }
    val anchors = mapOf(0f to 0, iconPx to 1)
    val scope = rememberCoroutineScope()

    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                scope.launch {
                    delay(2000L)
                    swipeableState.snapTo(0)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ ->
                    FractionalThreshold(0.5f)
                },
                orientation = Orientation.Horizontal
            )
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        swipeableState.snapTo(0)
                        onEvent(TasksEvent.OnDeleteTaskClick(task))
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = Color.White
                )
            }
        }

        AnimatedVisibility(
            visible = !task.isVisible,
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = TweenSpec(200, 0, FastOutLinearInEasing)
            )
        ) {
            Card(
                modifier = Modifier
                    .offset {
                        IntOffset(swipeableState.offset.value.roundToInt(), 0)
                    }
                    .fillMaxHeight(),
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
    }
}
