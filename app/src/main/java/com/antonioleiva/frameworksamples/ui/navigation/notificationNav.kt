package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.notifications.BasicNotificationScreen
import com.antonioleiva.frameworksamples.ui.screens.notifications.NotificationSamplesScreen

fun NavGraphBuilder.notificationNav(navController: NavHostController) {
    composable<NotificationSamplesScreen> {
        NotificationSamplesScreen(
            onSampleClick = { sample ->
                when (sample.destination) {
                    is BasicNotificationScreen -> navController.navigate(BasicNotificationScreen)
                }
            },
            onBack = { navController.popBackStack() }
        )
    }

    composable<BasicNotificationScreen> {
        BasicNotificationScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 