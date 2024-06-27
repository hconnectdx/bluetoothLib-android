package kr.co.hconnect.bluetoothlib_android.view.main

import android.bluetooth.le.ScanResult
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib_android.model.Permissions
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanListViewModel
import kr.co.hconnect.permissionlib.PermissionManager

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(navController: NavController, scanViewModel: ScanListViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Bluetooth Test App")
            })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting("블루투스 테스트 앱")
                ScanButton(scanViewModel)
                ScanList(scanViewModel, navController)
            }
        }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun ScanButton(scanViewModel: ScanListViewModel = viewModel()) {
    Button(onClick = {
        PermissionManager.launchPermissions(
            Permissions.PERMISSION_SDK_31,
        ) {
            val deniedItems = it.filter { p -> !p.value }
            if (deniedItems.isEmpty()) {

                HCBle.scanLeDevice { scanItem ->
                    val device = scanItem.scanRecord
                    if (device?.deviceName.isNullOrEmpty()) return@scanLeDevice

                    val scanResult: ScanResult? =
                        scanViewModel.scanList.find { vmItem ->
                            val address = vmItem.device.address
                            scanItem.device.address == address
                        }
                    if (scanResult == null) {
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