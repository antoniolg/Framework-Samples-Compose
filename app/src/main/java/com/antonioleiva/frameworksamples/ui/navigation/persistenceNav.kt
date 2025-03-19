package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.persistence.DataStoreScreen
import com.antonioleiva.frameworksamples.ui.screens.persistence.PersistenceSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.persistence.SharedPreferencesScreen

fun NavGraphBuilder.persistenceNav(navController: NavHostController) {
    composable<PersistenceSamplesScreen> {
        PersistenceSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }

    composable<SharedPreferencesScreen> {
        SharedPreferencesScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composable<DataStoreScreen> {
        DataStoreScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 