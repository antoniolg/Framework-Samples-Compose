package com.antonioleiva.frameworksamples.ui.navigation.theme

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.theme.CustomTypographyShapesScreen
import com.antonioleiva.frameworksamples.ui.screens.theme.ThemeSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.theme.ThemeSelectorScreen

fun NavGraphBuilder.themeNav(navController: NavHostController) {
    composable<ThemeSamplesScreen> {
        ThemeSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<ThemeSelectorScreen> {
        ThemeSelectorScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable<CustomTypographyShapesScreen> {
        CustomTypographyShapesScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 