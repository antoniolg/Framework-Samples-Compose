package com.antonioleiva.frameworksamples.ui.navigation

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.antonioleiva.frameworksamples.fragments.basic.BasicFragmentActivity
import com.antonioleiva.frameworksamples.fragments.navigation.NavigationActivity
import com.antonioleiva.frameworksamples.ui.screens.fragments.BasicFragmentSample
import com.antonioleiva.frameworksamples.ui.screens.fragments.FragmentsSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.fragments.NavigationFragmentSample

fun NavGraphBuilder.fragmentsNav(navController: NavHostController) {
    composable<FragmentsSamplesScreen> {
        FragmentsSamplesScreen(
            onSampleClick = { sample ->
                when (sample.destination) {
                    BasicFragmentSample -> {
                        val intent = Intent(navController.context, BasicFragmentActivity::class.java)
                        navController.context.startActivity(intent)
                    }
                    NavigationFragmentSample -> {
                        val intent = Intent(navController.context, NavigationActivity::class.java)
                        navController.context.startActivity(intent)
                    }
                }
            },
            onBack = { navController.popBackStack() }
        )
    }
} 