package kr.co.hconnect.bluetoothlib.handler

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log

class BleScanHandler {
    private val _leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d("BleScanHandler", "onScanResult: $result")
        }
    }
    val leScanCallback: ScanCallback
        get() = _leScanCallback
}