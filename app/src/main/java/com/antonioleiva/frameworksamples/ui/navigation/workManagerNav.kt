package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.workmanager.BasicSyncScreen
import com.antonioleiva.frameworksamples.ui.screens.workmanager.ImageProcessingScreen
import com.antonioleiva.frameworksamples.ui.screens.workmanager.WorkManagerSamplesScreen

fun NavGraphBuilder.workManagerNav(navController: NavHostController) {
    composable<WorkManagerSamplesScreen> {
        WorkManagerSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<BasicSyncScreen> {
        BasicSyncScreen(
            onBack = { navController.popBackStack() }
        )
    }
    
    composable<ImageProcessingScreen> {
        ImageProcessingScreen(
            onBack = { navController.popBackStack() }
        )
    }
}
