package com.github.grishberg.performance.launcher

import com.android.ddmlib.CollectingOutputReceiver
import com.android.ddmlib.IDevice
import com.android.ddmlib.IShellOutputReceiver
import com.android.ddmlib.InstallException
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.commands.CommandExecutionException
import java.util.concurrent.TimeUnit


class DeviceFacade(
        deviceWrapper: ConnectedDeviceWrapper
) {
    private val device: IDevice = deviceWrapper.device
    val serialNumber: String = device.serialNumber
    val name: String = device.name

    fun executeShellCommand(cmd: String) {
        device.executeShellCommand(cmd, CollectingOutputReceiver())
    }

    fun executeShellCommand(command: String, receiver: IShellOutputReceiver,
                            maxTimeout: Long, maxTimeToOutputResponse: Long,
                            maxTimeUnits: TimeUnit?) {
        device.executeShellCommand(command, receiver, maxTimeout, maxTimeToOutputResponse, maxTimeUnits)
    }

    fun executeShellCommandAndReturnOutput(command: String): String {
        return try {
            val receiver = CollectingOutputReceiver()
            executeShellCommand(command, receiver, 5L, TimeUnit.MINUTES)
            receiver.output
        } catch (e: Throwable) {
            throw CommandExecutionException("executeShellCommand exception:", e)
        }
    }

    fun executeShellCommand(command: String,
                            receiver: IShellOutputReceiver,
                            maxTimeToOutputResponse: Long,
                            maxTimeUnits: TimeUnit) {
        device.executeShellCommand(command, receiver, maxTimeToOutputResponse, maxTimeUnits)
    }


    @Throws(InstallException::class)
    fun installPackage(absolutePath: String, reinstall: Boolean, extraArgument: String?) {
        device.installPackage(absolutePath, reinstall, extraArgument)
    }
}