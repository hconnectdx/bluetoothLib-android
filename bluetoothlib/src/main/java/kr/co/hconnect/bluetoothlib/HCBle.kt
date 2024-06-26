package kr.co.hconnect.bluetoothlib

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log
import kr.co.hconnect.bluetoothlib.scan.BleScanHandler
import kr.co.hconnect.bluetoothlib.scan.ScanItem

object HCBle {
    private val TAG = "HCBle"
    private lateinit var appContext: Context

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private var scanning = false
    private val handler = Handler()

    fun init(context: Context) {
        if (::appContext.isInitialized) {
            Log.e(TAG, "appContext already to initialize")
            return
        }

        this.appContext = context
        bluetoothManager = appContext.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        Log.d(TAG, "BLE Initialized")
    }

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    @SuppressLint("MissingPermission")
    fun scanLeDevice(onScanResult: (ScanItem) -> Unit) {
        val scanHandler = BleScanHandler { result ->
            val scanItem = ScanItem(
                result.device.name ?: "",
                result.device.address,
                result.device.bondState
            )
            onScanResult(scanItem)
        }
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(scanHandler.leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner.startScan(scanHandler.leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(scanHandler.leScanCallback)
        }
    }

    fun connectToDevice() {
        
    }
}