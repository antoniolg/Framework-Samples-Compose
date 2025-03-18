package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.broadcast.BatteryScreen
import com.antonioleiva.frameworksamples.ui.screens.broadcast.BroadcastSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.broadcast.CustomBroadcastScreen

fun NavGraphBuilder.broadcastNav(navController: NavHostController) {
    composable<BroadcastSamplesScreen> {
        BroadcastSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<BatteryScreen> {
        BatteryScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable<CustomBroadcastScreen> {
        CustomBroadcastScreen(
            onBack = { navController.popBackStack() }
        )
    }
}