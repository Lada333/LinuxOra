package com.example.linuxora

import android.content.Context

object Preferences {

    private const val PREF_NAME = "timed_charging_preferences"

    const val TIMED_CHARGING_ENABLED = "timed_charging_enabled"

    fun pref(context: Context) = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


}