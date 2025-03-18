package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.coroutines.CoroutineSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.coroutines.DispatchersScreen

fun NavGraphBuilder.coroutineNav(navController: NavHostController) {
    composable<CoroutineSamplesScreen> {
        CoroutineSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<DispatchersScreen> {
        DispatchersScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 