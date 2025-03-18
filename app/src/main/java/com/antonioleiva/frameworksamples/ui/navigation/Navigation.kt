package com.antonioleiva.frameworksamples.ui.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Topic
import com.antonioleiva.frameworksamples.ui.screens.HomeScreen
import com.antonioleiva.frameworksamples.ui.screens.broadcast.BatteryScreen
import com.antonioleiva.frameworksamples.ui.screens.broadcast.BroadcastSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.broadcast.CustomBroadcastScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {
        composable<HomeScreen> {
            HomeScreen(
                topics = Topic.entries,
                onTopicClick = { topic ->
                    when (topic) {
                        Topic.BROADCAST_RECEIVERS -> navController.navigate(BroadcastSamplesScreen)
                        else -> navController.context.notImplementedToast()
                    }
                }
            )
        }

        broadcastNav(navController)
    }
}

private fun Context.notImplementedToast(): Unit = Toast.makeText(
    this, R.string.not_implemented_yet,
    Toast.LENGTH_SHORT
).show()