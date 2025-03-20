package com.antonioleiva.frameworksamples.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.databinding.ActivityNavigationBinding
import com.antonioleiva.frameworksamples.fragments.common.BaseChildActivity

/**
 * Activity that demonstrates the Navigation Component with Fragments:
 * - Navigation graph setup
 * - Navigation between fragments
 * - Passing arguments with Safe Args
 * - Animations during navigation
 */
class NavigationActivity : BaseChildActivity<ActivityNavigationBinding>() {

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityNavigationBinding {
        return ActivityNavigationBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.fragments_navigation_title)
        
        // Set up the action bar with the navigation controller
        setupActionBarWithNavController(navController)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        // Handle the up button by delegating to the nav controller
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
} 