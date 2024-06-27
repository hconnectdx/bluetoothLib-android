package kr.co.hconnect.bluetoothlib_android.view.main

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanListViewModel

@Composable
fun ScanList(
    scanViewModel: ScanListViewModel = viewModel(),
    navController: NavController
) {
    val scanList = scanViewModel.scanList
    LazyColumn(contentPadding = PaddingValues(horizontal = 15.dp)) {
        items(scanList.size) { index ->
            ListItem(
                selItem = scanList[index],
                scanViewModel = scanViewModel,
                navController = navController,
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ListItem(
    selItem: ScanResult,
    scanViewModel: ScanListViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .clickable {
                scanViewModel.selDevice.value = selItem
                HCBle.scanStop()
                navController.navigate("detail")
            },
    ) {
        Text(
            text = selItem.device.name ?: "Unknown Device",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}