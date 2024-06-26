package kr.co.hconnect.bluetoothlib.scan

data class ScanItem(
    val deviceName: String,
    val deviceAddress: String,
    val bondState: Int
) {
    companion object {
        fun empty(): ScanItem {
            return ScanItem(
                deviceName = "",
                deviceAddress = "",
                bondState = -1
            )
        }
    }
}