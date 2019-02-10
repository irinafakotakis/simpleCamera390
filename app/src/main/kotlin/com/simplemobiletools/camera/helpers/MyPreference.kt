package com.simplemobiletools.camera.helpers

import android.content.Context

class MyPreference(context: Context){
    val PREFERENCE_NAME= "SharedPreferenceExample"
    val PREFERENCE_COLOR = "DockColor"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getDockerColor() : Int {
        return preference.getInt(PREFERENCE_COLOR, -7508381)
    }

    fun setDockerColor(color:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_COLOR, color)
        editor.apply()
    }
}
