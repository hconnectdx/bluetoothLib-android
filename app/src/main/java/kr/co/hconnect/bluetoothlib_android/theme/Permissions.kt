package kr.co.hconnect.bluetoothlib_android.theme

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Permissions {
    @RequiresApi(Build.VERSION_CODES.S)
    val PERMISSION_SDK_31 = listOf<String>(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )

    val PERMISSION_SDK_30 = listOf<String>(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
    )
}