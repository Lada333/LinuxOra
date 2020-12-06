package com.example.linuxora

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import java.util.*

class NextAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?:return
        if(intent?.action != AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED)return

        if(!Preferences.pref(context).getBoolean(Preferences.TIMED_CHARGING_ENABLED, false)) return

        ContextCompat.getSystemService(context, AlarmManager::class.java)?.let { manager ->
            val nextAlarm = manager.nextAlarmClock
            if (nextAlarm == null) {
                BatteryChargeChanger.setBatteryChargeOn()
                Alarm.removeSentIntents(context)
                return
            }
            if (nextAlarm.triggerTime >= System.currentTimeMillis() + Alarm.MASFEL_ORA) {
                BatteryChargeChanger.setBatteryChargeOff()
                Alarm.sendIntents(context, nextAlarm)
            }else{
                BatteryChargeChanger.setBatteryChargeOn()
                Alarm.sendTimedChargingIntent(context, nextAlarm)
            }
        }
    }

}