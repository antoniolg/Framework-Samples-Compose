package com.antonioleiva.frameworksamples.ui.screens.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.antonioleiva.frameworksamples.R
import java.util.Locale

class LocaleChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_LOCALE_CHANGED) {
            val currentLocale = Locale.getDefault()
            val message = context.getString(R.string.language_changed, currentLocale.displayLanguage)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
} 