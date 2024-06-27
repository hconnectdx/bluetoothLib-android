package kr.co.hconnect.bluetoothlib_android.view

import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib.gatt.BLEState
import kr.co.hconnect.bluetoothlib_android.R
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(navController: NavController, scanViewModel: ScanListViewModel = viewModel()) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Device Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    ConnectButton(scanViewModel)
                }

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
        ) {
            scanViewModel.selDevice
            DeviceDetailContent(scanViewModel)
        }
    }
}


@Composable
private fun ConnectButton(scanViewModel: ScanListViewModel) {
    IconButton(onClick = {
        scanViewModel.isBonded.let {
            val state = it.intValue
            when (state) {
                BLEState.BOND_BONDED -> {
                    Log.d("GATTService", "Disconnect")
                    HCBle.disconnect {
                        scanViewModel.connectState.intValue = BLEState.STATE_DISCONNECTED
                        scanViewModel.isBonded.intValue = BLEState.BOND_NONE
                    }
                }

                else -> {
                    Log.d("GATTService", "Connect")
                    scanViewModel.selDevice.value?.let { it1 -> connect(it1, scanViewModel) }
                }
            }

        }
    }) {
        val connState = scanViewModel.connectState.intValue
        val bondedState = scanViewModel.isBonded.intValue
        BLEConnStateIcon(bondedState, connState)
    }
}

private fun connect(
    it: ScanResult,
    scanViewModel: ScanListViewModel
) {
    HCBle.connectToDevice(
        it.device,
        onConnState = { newState ->
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("GATTService", "STATE_CONNECTED")
                }

                BluetoothProfile.STATE_CONNECTING -> {
                    Log.d("GATTService", "STATE_CONNECTING")
                }

                BluetoothProfile.STATE_DISCONNECTING -> {
                    Log.d("GATTService", "STATE_DISCONNECTING")
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("GATTService", "STATE_DISCONNECTED")
                }
            }
            scanViewModel.connectState.intValue = newState
        },
        onGattServiceState = { it ->
            Log.d("GATTService", "onGattServiceState: $it")
        },
        onBondState = { bondedState ->
            scanViewModel.isBonded.value = bondedState
        },
        onReceive = { data ->
            Log.d("GATTService", "onReceive: ${data.value}")
        },
    )
}

@Composable
private fun BLEConnStateIcon(bondedState: Int, connState: Int) {
    val painter = painter(bondedState, connState)
    Image(painter = painter, contentDescription = "what?")
}

@Composable
private fun painter(
    bondedState: Int,
    connState: Int
): Painter {
    val state = BLEState.getBondedStateWithConnection(bondedState, connState)
    val painter = when (state) {
        BLEState.CONNECTED_BONDED -> painterResource(id = R.drawable.ico_ble_on)
        BLEState.CONNECTING_BONDING -> painterResource(id = R.drawable.ico_ble_connecting)
        else -> painterResource(id = R.drawable.ico_ble_off)
    }
    return painter
}


@Composable
@Preview
fun DeviceDetailContent(scanViewModel: ScanListViewModel = viewModel()) {
    val device = scanViewModel.selDevice.value
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Device Name: ${device?.scanRecord?.deviceName ?: "Unknown"}",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Device Address: ${device?.device?.address}")

            }
        }

        HorizontalDivider(Modifier.padding(16.dp))

        if (scanViewModel.connectState.intValue == BluetoothProfile.STATE_CONNECTED) {
            GattServiceList(HCBle.getGattServiceList())
        }
    }
}

@Composable
fun GattServiceList(
    gattServiceList: List<BluetoothGattService>
) {
    var expandedServiceUuid by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        gattServiceList.forEach { service ->

            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                Text(text = service.uuid.toString(), modifier = Modifier.clickable {
                    HCBle.setServiceUUID(service.uuid.toString())
                    if (expandedServiceUuid == service.uuid.toString()) {
                        expandedServiceUuid = null
                    } else {
                        expandedServiceUuid = service.uuid.toString()
                    }
                })
                if (expandedServiceUuid == service.uuid.toString()) {
                    service.characteristics.forEach { characteristic ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = characteristic.uuid.toString(),
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 4.dp)
                                    .weight(5f)
                                    .clickable {
                                        HCBle.setCharacteristicUUID(characteristic.uuid.toString())
                                    }
                            )
                            IconButton(
                                onClick = {
                                    HCBle.setCharacteristicNotification(true)

                                }, modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun HorizontalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.Gray)
    )
}