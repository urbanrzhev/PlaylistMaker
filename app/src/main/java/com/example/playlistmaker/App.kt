package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
 const val MY_SWITCHER_PREFERENCES = "my_switcher_preferences"
private const val MY_KEY_SWITCHER = "my_key_switcher"
const val MY_KEY_SWITCHER2 = "my_key_switcher2"

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
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        setMyTheme(darkTheme)
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
