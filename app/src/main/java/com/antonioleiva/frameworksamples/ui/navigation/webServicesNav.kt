package com.antonioleiva.frameworksamples.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.ui.screens.webservices.BasicRetrofitScreen
import com.antonioleiva.frameworksamples.ui.screens.webservices.CrudOperationsScreen
import com.antonioleiva.frameworksamples.ui.screens.webservices.WebServicesSamplesScreen

fun NavGraphBuilder.webServicesNav(navController: NavHostController) {
    composable<WebServicesSamplesScreen> {
        WebServicesSamplesScreen(
            onSampleClick = { navController.navigate(it.destination) },
            onBack = { navController.popBackStack() }
        )
    }
    
    composable<BasicRetrofitScreen> {
        BasicRetrofitScreen(
            onBack = { navController.popBackStack() }
        )
    }
    
    composable<CrudOperationsScreen> {
        CrudOperationsScreen(
            onBack = { navController.popBackStack() }
        )
    }
} 