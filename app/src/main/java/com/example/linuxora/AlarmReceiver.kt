package com.example.linuxora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.edit

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?:return
        /*Log.d("Ébresztő", intent?.action.orEmpty())*/
        when(intent?.action){
            Alarm.BATTERY_CHARGE_ACTION -> BatteryChargeChanger.setBatteryChargeOn()
            Alarm.TIMED_CHARGING_CHANGE_ACTION -> Preferences.pref(context).edit { putBoolean(Preferences.TIMED_CHARGING_ENABLED, false) }
        }
    }

}