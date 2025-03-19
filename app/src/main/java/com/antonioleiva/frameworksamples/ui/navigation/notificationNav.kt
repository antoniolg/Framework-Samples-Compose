package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.notifications.ActionsNotificationScreen
import com.antonioleiva.frameworksamples.ui.screens.notifications.BasicNotificationScreen
import com.antonioleiva.frameworksamples.ui.screens.notifications.NotificationSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.services.DownloadServiceScreen

fun NavGraphBuilder.notificationNav(navController: NavHostController) {
    composable<NotificationSamplesScreen> {
        NotificationSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<BasicNotificationScreen> {
        BasicNotificationScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable<ActionsNotificationScreen> {
        ActionsNotificationScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable<DownloadServiceScreen> {
        DownloadServiceScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 