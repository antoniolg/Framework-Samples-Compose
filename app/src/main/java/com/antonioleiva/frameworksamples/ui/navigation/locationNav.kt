package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.location.BasicLocationScreen
import com.antonioleiva.frameworksamples.ui.screens.location.LocationMapsSamplesScreen

fun NavGraphBuilder.locationNav(navController: NavHostController) {
    composable<LocationMapsSamplesScreen> {
        LocationMapsSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }
    
    composable<BasicLocationScreen> {
        BasicLocationScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 