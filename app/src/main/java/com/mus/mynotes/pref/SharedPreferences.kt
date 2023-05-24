package com.mus.mynotes.pref

import android.content.SharedPreferences
import com.mus.mynotes.dal.di.AppModule
import java.util.*

lateinit var prefs: SharedPreferences

// add any type of entry in shared preference
fun SharedPreferences.put(name: String, any: Any) {
    when (any) {
        is String -> edit().putString(name, any).apply()
        is Int -> edit().putInt(name, any).apply()
        is Long -> edit().putLong(name, any).apply()
        is Boolean -> edit().putBoolean(name, any).apply()
        is Objects -> edit().putString(name, AppModule.provideGson().toJson(any)).apply()
        // also accepts Float, Long & StringSet
    }
}

fun SharedPreferences.removePrefs() {
    prefs.edit().clear().apply()
}
