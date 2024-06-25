package kr.co.hconnect.bluetoothlib

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.os.Handler
import android.util.Log
import kr.co.hconnect.bluetoothlib.handler.BleScanHandler

object HCBle {

    private lateinit var appContext: Context

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private var scanning = false
    private val handler = Handler()

    fun init(context: Context) {
        if (::appContext.isInitialized) {
            Log.e("BLEController", "appContext already to initialize")
            return
        }

        this.appContext = context
        bluetoothManager = appContext.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        val scanHandler = BleScanHandler()
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(scanHandler.leScanCallback)
            }, SCAN_PERIOD)
            Log.d("HCBle", "ㅅㅅㅅㅅㅅㅅㅅ")
            scanning = true
            bluetoothLeScanner.startScan(scanHandler.leScanCallback)
        } else {
            Log.d("HCBle", "ㄴㄴㄴㄴㄴㄴㄴㄴ")
            scanning = false
            bluetoothLeScanner.stopScan(scanHandler.leScanCallback)
        }
    }
}