package kr.co.hconnect.bluetoothlib_android.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import kr.co.hconnect.bluetoothlib.scan.ScanItem

class ScanViewModel : ViewModel() {
    var scanList = mutableStateListOf(ScanItem.empty())
}