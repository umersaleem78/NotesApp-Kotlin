package com.mus.mynotes.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.mus.mynotes.R
import com.mus.mynotes.constants.AppConstants
import com.mus.mynotes.pref.prefs
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

@HiltAndroidApp
class MyNotesApp :Application(){

    override fun onCreate() {
        super.onCreate()
        appContext = this
        // set no dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // initialize Preferences
        prefs = getSharedPreferences(AppConstants.PREFERENCES, Context.MODE_PRIVATE)
        // setting custom default font for the app
        setDefaultCustomFont()
    }

    private fun setDefaultCustomFont() {
        // fetch font
        val font = CalligraphyConfig.Builder()
            .setDefaultFontPath(getString(R.string.roboto_condensed_regular))
            .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
            .build()
        // set custom font as default
        ViewPump.init(ViewPump.builder().addInterceptor(CalligraphyInterceptor(font)).build())
    }

    companion object {
        var appContext: Application? = null
            private set
    }
}