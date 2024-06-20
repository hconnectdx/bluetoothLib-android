package kr.co.hconnect.bluetoothlib

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
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

        scanLeDevice()
    }

    // 블루투스 활성화
//    fun activateBluetooth() {
//        if (bluetoothAdapter.isEnabled == false) {
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            (activity as Activity).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//            appContext.startActi
//
//        }
//    }




    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000
    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        val scanHandler = BleScanHandler()
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                if (ActivityCompat.checkSelfPermission(
                        this.appContext,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d("HCBle", "Permission denied")
                    return@postDelayed
                }
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