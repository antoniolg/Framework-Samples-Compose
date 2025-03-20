package com.antonioleiva.frameworksamples.fragments.basic

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.commit
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.databinding.ActivityBasicFragmentBinding
import com.antonioleiva.frameworksamples.fragments.common.BaseChildActivity

/**
 * Activity that demonstrates basic Fragment usage:
 * - Static Fragment declaration in XML
 * - Dynamic Fragment management with FragmentManager
 * - Fragment transactions (add, replace, remove)
 * - Back stack management
 * - Passing arguments to Fragments
 */
class BasicFragmentActivity : BaseChildActivity<ActivityBasicFragmentBinding>() {

    override fun inflateBinding(inflater: LayoutInflater): ActivityBasicFragmentBinding {
        return ActivityBasicFragmentBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.fragments_basic_title)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Add Fragment button
        binding.btnAddFragment.setOnClickListener {
            val message = binding.etArgument.text.toString().takeIf { it.isNotEmpty() } ?: "Default"
            val fragment = DynamicFragment.newInstance(message)
            
            supportFragmentManager.commit {
                if (binding.cbAddToBackstack.isChecked) {
                    addToBackStack("add_fragment")
                }
                add(R.id.fragment_container, fragment)
            }
        }
        
        // Replace Fragment button
        binding.btnReplaceFragment.setOnClickListener {
            val message = binding.etArgument.text.toString().takeIf { it.isNotEmpty() } ?: "Default"
            val fragment = DynamicFragment.newInstance(message)
            
            supportFragmentManager.commit {
                if (binding.cbAddToBackstack.isChecked) {
                    addToBackStack("replace_fragment")
                }
                replace(R.id.fragment_container, fragment)
            }
        }
        
        // Remove Fragment button
        binding.btnRemoveFragment.setOnClickListener {
            // Find the current fragment in the container
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            
            if (currentFragment != null) {
                supportFragmentManager.commit {
                    if (binding.cbAddToBackstack.isChecked) {
                        addToBackStack("remove_fragment")
                    }
                    remove(currentFragment)
                }
            }
        }
    }
} 