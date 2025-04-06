package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

private const val MY_SWITCHER_FOR_THEME_PREFERENCES = "my_switcher_preferences"
private const val MY_KEY_SWITCHER_FOR_THEME = "my_key_switcher"

class App : Application() {
    var darkTheme = false
    private var sharedPrefsForTheme: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPrefsForTheme = getSharedPreferences(MY_SWITCHER_FOR_THEME_PREFERENCES, MODE_PRIVATE)
        darkTheme = getMyTheme()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                Log.v("my","AppCompatDelegate.MODE_NIGHT_YES")
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                Log.v("my","AppCompatDelegate.MODE_NIGHT_NO")
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
            //setMyTheme(darkTheme)
    }

    private fun getMyTheme(): Boolean {
        return sharedPrefsForTheme?.getString(MY_KEY_SWITCHER_FOR_THEME, "false").toBoolean()
    }

    private fun setMyTheme(checked: Boolean) {
        sharedPrefsForTheme?.edit()
            ?.putString(MY_KEY_SWITCHER_FOR_THEME, checked.toString())
            ?.apply()
    }
}