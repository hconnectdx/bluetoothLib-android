package kr.co.hconnect.bluetoothlib_android.viewmodel

import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ScanListViewModel : ViewModel() {
    var scanList = mutableStateListOf<ScanResult>()
}