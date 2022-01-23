package com.asrieltiago.tasksmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asrieltiago.tasksmanagement.ui.add_edit_task.AddEditTaskScreen
import com.asrieltiago.tasksmanagement.ui.task_list.TaskListScreen
import com.asrieltiago.tasksmanagement.ui.theme.TasksManagementTheme
import com.asrieltiago.tasksmanagement.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksManagementTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.TASKS_LIST
                ) {
                    composable(Routes.TASKS_LIST) {
                        TaskListScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                    composable(
                        Routes.ADD_EDIT_TASK + "?taskId={taskId}",
                        arguments = listOf(
                            navArgument(name = "taskId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditTaskScreen(onPopBackstack = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TasksManagementTheme {
        Greeting("Android")
    }
}