package com.antonioleiva.frameworksamples.ui.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Topic
import com.antonioleiva.frameworksamples.ui.navigation.theme.themeNav
import com.antonioleiva.frameworksamples.ui.screens.HomeScreen

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
                    when (topic.destination) {
                        Unit -> navController.context.notImplementedToast()
                        else -> navController.navigate(topic.destination)
                    }
                }
            )
        }

        broadcastNav(navController)
        serviceNav(navController)
        coroutineNav(navController)
        notificationNav(navController)
        persistenceNav(navController)
        workManagerNav(navController)
        webServicesNav(navController)
        fragmentsNav(navController)
        locationNav(navController)
        themeNav(navController)
    }
}

internal fun Context.notImplementedToast(): Unit = Toast.makeText(
    this, R.string.not_implemented_yet,
    Toast.LENGTH_SHORT
).show()