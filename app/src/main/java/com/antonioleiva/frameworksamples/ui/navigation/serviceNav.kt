package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.services.BoundDownloadServiceScreen
import com.antonioleiva.frameworksamples.ui.screens.services.DownloadServiceScreen
import com.antonioleiva.frameworksamples.ui.screens.services.ServiceSamplesScreen

fun NavGraphBuilder.serviceNav(navController: NavHostController) {
    composable<ServiceSamplesScreen> {
        ServiceSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<DownloadServiceScreen> {
        DownloadServiceScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable<BoundDownloadServiceScreen> {
        BoundDownloadServiceScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 