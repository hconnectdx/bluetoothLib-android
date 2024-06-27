package kr.co.hconnect.bluetoothlib.gatt

object BLEState {
    const val BOND_NONE = 10
    const val BOND_BONDING = 11
    const val BOND_BONDED = 12

    const val STATE_DISCONNECTED = 0
    const val STATE_CONNECTING = 1
    const val STATE_CONNECTED = 2
    const val STATE_DISCONNECTING = 3

    const val GATT_SUCCESS = 0
    const val GATT_READ_NOT_PERMITTED = 0x2
    const val GATT_WRITE_NOT_PERMITTED = 0x3
    const val GATT_INSUFFICIENT_AUTHENTICATION = 0x5
    const val GATT_REQUEST_NOT_SUPPORTED = 0x6
    const val GATT_INSUFFICIENT_ENCRYPTION = 0xf
    const val GATT_INVALID_OFFSET = 0x7
    const val GATT_INSUFFICIENT_AUTHORIZATION = 0x8
    const val GATT_INVALID_ATTRIBUTE_LENGTH = 0xd
    const val GATT_CONNECTION_CONGESTED = 0x8f
    const val GATT_FAILURE = 0x101


    const val NOT_CONNECTED_BONDED = 0
    const val CONNECTED_BONDED = 1
    const val CONNECTING_BONDING = 2

    fun getBondedStateWithConnection(bondState: Int, connectionState: Int): Int {
        return when {
            bondState == BOND_BONDED && connectionState == STATE_CONNECTED -> CONNECTED_BONDED
            bondState == BOND_NONE && connectionState == STATE_DISCONNECTED -> NOT_CONNECTED_BONDED
            bondState == BOND_BONDING || connectionState == STATE_CONNECTING -> CONNECTING_BONDING
            bondState == BOND_BONDED && connectionState != STATE_CONNECTED -> CONNECTING_BONDING
            bondState != BOND_BONDED && connectionState == STATE_CONNECTED -> CONNECTING_BONDING
            else -> NOT_CONNECTED_BONDED
        }
    }
}