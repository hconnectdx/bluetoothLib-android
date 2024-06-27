package kr.co.hconnect.bluetoothlib.gatt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.util.Log
import kr.co.hconnect.bluetoothlib.HCBle

@SuppressLint("MissingPermission")
class GATTService {
    private lateinit var gattServiceList: List<BluetoothGattService>
    private lateinit var selService: BluetoothGattService
    private lateinit var selCharacteristic: BluetoothGattCharacteristic

    fun getGattServiceList(): List<BluetoothGattService> {
        return gattServiceList
    }

    fun setGattServiceList(gattServiceList: List<BluetoothGattService>) {
        this.gattServiceList = gattServiceList
    }

    fun setServiceUUID(uuid: String) {
        gattServiceList.find { it.uuid.toString() == uuid }?.let {
            Log.d("GATTService", "Service UUID: $uuid")
            selService = it
        }

    }

    fun setCharacteristicUUID(characteristicUUID: String) {
        selService.characteristics.find { it.uuid.toString() == characteristicUUID }?.let {
            Log.d("GATTService", "Characteristic UUID: $characteristicUUID")
            selCharacteristic = it
        }
    }

    fun readCharacteristic() {
        HCBle.bluetoothGatt.readCharacteristic(selCharacteristic)
    }

    fun writeCharacteristic(data: ByteArray) {
        selCharacteristic.value = data
        HCBle.bluetoothGatt.writeCharacteristic(selCharacteristic)
    }

    fun setCharacteristicNotification(isEnable: Boolean) {
        HCBle.bluetoothGatt.setCharacteristicNotification(selCharacteristic, isEnable)
    }
}