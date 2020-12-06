package com.example.linuxora

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.edit

object Alarm {


    fun sendIntents(context: Context, nextAlarm:AlarmManager.AlarmClockInfo){
        val chargeBattery = composeIntent(context, BATTERY_CHARGE_REQUEST_CODE, BATTERY_CHARGE_ACTION)
        val timedCharging = composeIntent(context, TIMED_CHARGING_REQUEST_CODE, TIMED_CHARGING_CHANGE_ACTION)

        ContextCompat.getSystemService(context, AlarmManager::class.java)?.let {
            val chargeNotify = nextAlarm.triggerTime - MASFEL_ORA
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) it.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, chargeNotify, chargeBattery)
            else it.setExact(AlarmManager.RTC_WAKEUP, chargeNotify, chargeBattery)

            val timedNotify = nextAlarm.triggerTime - 60_000L
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) it.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timedNotify, timedCharging)
            else it.setExact(AlarmManager.RTC_WAKEUP, timedNotify, timedCharging)
        }

    }

    fun sendTimedChargingIntent(context: Context, nextAlarm: AlarmManager.AlarmClockInfo){
        val timedCharging = composeIntent(context, TIMED_CHARGING_REQUEST_CODE, TIMED_CHARGING_CHANGE_ACTION)
        ContextCompat.getSystemService(context, AlarmManager::class.java)?.let {
            val timedNotify = nextAlarm.triggerTime - 60_000L
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) it.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timedNotify, timedCharging)
            else it.setExact(AlarmManager.RTC_WAKEUP, timedNotify, timedCharging)
        }
    }

    fun removeSentIntents(context: Context){
        ContextCompat.getSystemService(context, AlarmManager::class.java)?.let {
            val chargeBattery = composeIntent(context, BATTERY_CHARGE_REQUEST_CODE, BATTERY_CHARGE_ACTION)
            val timedCharging = composeIntent(context, TIMED_CHARGING_REQUEST_CODE, TIMED_CHARGING_CHANGE_ACTION)

            it.cancel(chargeBattery)
            it.cancel(timedCharging)
        }
    }


    private fun composeIntent(context: Context, requestCode: Int, action: String): PendingIntent {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(context, requestCode , alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    const val BATTERY_CHARGE_ACTION = "com.example.linuxora.battery_charge_action"
    const val TIMED_CHARGING_CHANGE_ACTION = "com.example.linuxora.timed_charging_change_action"

    const val BATTERY_CHARGE_REQUEST_CODE = 7674
    const val TIMED_CHARGING_REQUEST_CODE = 5874


    const val MASFEL_ORA = 5_400_000L //1000*60*60*1.5

}
