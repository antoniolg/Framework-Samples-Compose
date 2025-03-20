package com.antonioleiva.frameworksamples.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.antonioleiva.frameworksamples.R

abstract class BaseActivity<Binding : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.root.findViewById<Toolbar>(R.id.toolbar))
        findViewById<View>(R.id.main).applyInsets()
    }

    abstract fun inflateBinding(inflater: LayoutInflater): Binding
}

abstract class BaseChildActivity<Binding : ViewBinding> : BaseActivity<Binding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}

private fun View.applyInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}