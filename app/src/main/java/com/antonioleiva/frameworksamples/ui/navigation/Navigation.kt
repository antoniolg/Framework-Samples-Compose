package com.antonioleiva.frameworksamples.ui.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Topic
import com.antonioleiva.frameworksamples.ui.screens.HomeScreen
import com.antonioleiva.frameworksamples.ui.screens.broadcast.BroadcastSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.coroutines.CoroutineSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.notifications.NotificationSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.services.ServiceSamplesScreen

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
                        Topic.SERVICES -> navController.navigate(ServiceSamplesScreen)
                        Topic.COROUTINES -> navController.navigate(CoroutineSamplesScreen)
                        Topic.NOTIFICATIONS -> navController.navigate(NotificationSamplesScreen)
                        else -> navController.context.notImplementedToast()
                    }
                }
            )
        }

        broadcastNav(navController)
        serviceNav(navController)
        coroutineNav(navController)
        notificationNav(navController)
    }
}

private fun Context.notImplementedToast(): Unit = Toast.makeText(
    this, R.string.not_implemented_yet,
    Toast.LENGTH_SHORT
).show()