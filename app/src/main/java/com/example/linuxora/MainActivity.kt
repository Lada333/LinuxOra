package com.example.linuxora

import android.app.AlarmManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import com.example.linuxora.Alarm.MASFEL_ORA

class MainActivity : AppCompatActivity() {

    lateinit var timedChargingSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timedChargingSwitch = findViewById(R.id.service_enabled)

        setSwitchAccordingly()
    }

    private fun setSwitchAccordingly() {
        timedChargingSwitch.isChecked =
            Preferences.pref(this).getBoolean(Preferences.TIMED_CHARGING_ENABLED, false)
        timedChargingSwitch.setOnCheckedChangeListener { _, isChecked ->
            Preferences.pref(this).edit { putBoolean(Preferences.TIMED_CHARGING_ENABLED, isChecked) }
            if (isChecked) checkAlarm()
            else Alarm.removeSentIntents(this)
        }
    }

    private fun checkAlarm() {
        ContextCompat.getSystemService(this, AlarmManager::class.java)?.let { manager ->
            val nextAlarm = manager.nextAlarmClock
            if (nextAlarm == null) {
                BatteryChargeChanger.setBatteryChargeOn()
                Alarm.removeSentIntents(this)
                return
            }
            if (nextAlarm.triggerTime >= System.currentTimeMillis() + Alarm.MASFEL_ORA) {
                BatteryChargeChanger.setBatteryChargeOff()
                Alarm.sendIntents(this, nextAlarm)
            }else{
                BatteryChargeChanger.setBatteryChargeOn()
                Alarm.sendTimedChargingIntent(this, nextAlarm)
            }
        }
    }


}