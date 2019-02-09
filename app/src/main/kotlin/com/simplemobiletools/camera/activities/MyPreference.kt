package com.simplemobiletools.camera.activities

import android.content.Context

class MyPreference(context: Context){
    val PREFERENCE_NAME= "SharedPreferenceExample"
    val PREFERENCE_COLOR = "DockColor"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getLoginCount() : Int {
        return preference.getInt(PREFERENCE_COLOR, -7508381)
    }

    fun setLoginCount(color:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_COLOR, color)
        editor.apply()
    }
}
