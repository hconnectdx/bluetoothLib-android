package kr.co.hconnect.bluetoothlib_android.view

import android.util.Log
import android.widget.Toast
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
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib.scan.ScanItem
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanViewModel

@Composable
fun ScanList(viewModel: ScanViewModel = viewModel()) {
    val scanList = viewModel.scanList
    LazyColumn(contentPadding = PaddingValues(horizontal = 15.dp)) {
        items(scanList.size) { index ->
            ListItem(item = scanList[index])
        }
    }
}

@Composable
fun ListItem(item: ScanItem) {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .clickable {
                Log.d("ScanList", "Item Clicked: $item")
                HCBle.connectToDevice()
            },
    ) {
        Text(
            text = item.deviceName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}