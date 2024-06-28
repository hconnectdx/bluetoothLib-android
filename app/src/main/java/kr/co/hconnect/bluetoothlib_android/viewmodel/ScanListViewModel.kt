package kr.co.hconnect.bluetoothlib_android.viewmodel

import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kr.co.hconnect.bluetoothlib.gatt.BLEState

class ScanListViewModel : ViewModel() {
    var scanList = mutableStateListOf<ScanResult>()
    var selDevice = mutableStateOf<ScanResult?>(null)
    var connectState = mutableIntStateOf(BluetoothProfile.STATE_DISCONNECTED)
    var isBonded = mutableIntStateOf(BLEState.BOND_NONE)
    var isGattConnected = mutableIntStateOf(BLEState.GATT_FAILURE)
}