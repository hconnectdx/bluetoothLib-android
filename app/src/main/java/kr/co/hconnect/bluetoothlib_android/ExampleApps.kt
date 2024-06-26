package kr.co.hconnect.bluetoothlib_android

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib.scan.ScanItem
import kr.co.hconnect.permissionlib.PermissionManager
import kr.co.hconnect.myapplication123.ui.theme.MyApplicationTheme
import kr.co.hconnect.bluetoothlib_android.view.ScanList
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanViewModel

class ExampleApps : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemsList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        val scanViewModel = ScanViewModel()
                        Greeting("블루투스 테스트 앱")
                        ScanButton(scanViewModel)
                        ScanList(scanViewModel)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    private fun ScanButton(scanViewModel: ScanViewModel = viewModel()) {

        Button(onClick = {
            PermissionManager.launchPermissions(
                Permissions.PERMISSION_SDK_31,
            ) {
                val deniedItems = it.filter { p -> !p.value }
                if (deniedItems.isEmpty()) {

                    HCBle.scanLeDevice { scanItem ->
                        val item: ScanItem? =
                            scanViewModel.scanList.find { vmItem ->
                                vmItem.deviceName == scanItem.deviceName || scanItem.deviceName.isEmpty()
                            }

                        if (item == null) {
                            scanViewModel.scanList.add(scanItem)
                        }
                    }
                } else {
                    Log.e("ExampleApps", "Permission Denied: $deniedItems")
                }
            }
        }) {
            Text("Scan Bluetooth Devices")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStart() {
        super.onStart()

        // 퍼미션 런처 등록
        PermissionManager.registerPermissionLauncher(this)

        // BLE 초기화
        HCBle.init(this@ExampleApps)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}