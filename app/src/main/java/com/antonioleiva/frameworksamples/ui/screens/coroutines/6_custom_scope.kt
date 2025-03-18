package com.antonioleiva.frameworksamples.ui.screens.coroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class SampleActivity : ComponentActivity() {

    private lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scope = CoroutineScope(Dispatchers.Main)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}