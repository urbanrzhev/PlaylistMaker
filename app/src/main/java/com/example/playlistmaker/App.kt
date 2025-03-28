package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
const val MY_SWITCHER_PREFERENCES = "my_switcher_preferences"
const val MY_KEY_SWITCHER = "my_key_switcher"
class App : Application() {
    private var darkTheme = false
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(MY_SWITCHER_PREFERENCES, MODE_PRIVATE)
        darkTheme = getMyTheme()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                setMyTheme(true)
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                setMyTheme(false)
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    fun getMyTheme():Boolean{
        return sharedPrefs?.getString(MY_KEY_SWITCHER,"false").toBoolean()
    }
    private fun setMyTheme(checked:Boolean){
        sharedPrefs?.edit()
            ?.putString(MY_KEY_SWITCHER, checked.toString())
            ?.apply()
    }

}