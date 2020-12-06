package com.example.linuxora

import com.topjohnwu.superuser.Shell
import java.util.concurrent.Executors

object BatteryChargeChanger {

    private val executor = Executors.newSingleThreadExecutor()

    fun setBatteryChargeOn() = execute(true)

    fun setBatteryChargeOff() = execute(false)

    private fun execute(charging: Boolean) {
        val command ="echo '${if(charging) 1 else 0}' > /sys/class/power_supply/battery/charging_enabled"
        executor.execute { Shell.su(command).exec() }
    }

}