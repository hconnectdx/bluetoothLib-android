package kr.co.hconnect.bluetoothlib_android.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BondedDevicesViewModel : ViewModel() {
    val bondedDevices = mutableStateOf<List<BluetoothDevice>>(emptyList())
}