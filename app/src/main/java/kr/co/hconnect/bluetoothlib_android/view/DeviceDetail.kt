package kr.co.hconnect.bluetoothlib_android.view

import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib.gatt.BLEState
import kr.co.hconnect.bluetoothlib_android.R
import kr.co.hconnect.bluetoothlib_android.viewmodel.DeviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    navController: NavController,
    deviceViewModel: DeviceViewModel = viewModel()
) {
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
                    ConnectButton(deviceViewModel)
                }

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
        ) {
            deviceViewModel.device.value
            DeviceDetailContent(deviceViewModel, navController)
        }
    }
}


@Composable
private fun ConnectButton(deviceViewModel: DeviceViewModel) {
    IconButton(onClick = {
        deviceViewModel.isBonded.let {
            val state = it.intValue
            when (state) {
                BLEState.BOND_BONDED -> {
                    Log.d("GATTService", "Disconnect")
                    HCBle.disconnect {
                        deviceViewModel.apply {
                            isBonded.intValue = BLEState.BOND_NONE
                            connectState.intValue = BLEState.STATE_DISCONNECTED
                            isGattConnected.intValue = BLEState.GATT_FAILURE
                        }
                    }
                }

                else -> {
                    Log.d("GATTService", "Connect")
                    connect(deviceViewModel)
                }
            }

        }
    }) {
        val connState = deviceViewModel.connectState.intValue
        val bondedState = deviceViewModel.isBonded.intValue
        BLEConnStateIcon(bondedState, connState)
    }
}

private fun connect(
    deviceViewModel: DeviceViewModel
) {
    HCBle.connectToDevice(
        deviceViewModel.device.value!!,
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
            deviceViewModel.connectState.intValue = newState
        },
        onGattServiceState = { it ->
            Log.d("GATTService", "onGattServiceState: $it")
            deviceViewModel.isGattConnected.intValue = it
        },
        onBondState = { bondedState ->
            deviceViewModel.isBonded.intValue = bondedState
        },
        onSubscriptionState = { state ->
            Log.d("GATTService", "onSubscriptionState: $state")
        },
        onReceive = { data ->
            Log.d("GATTService", "onReceive: ${data.value}")
        },
    )
}

@Composable
private fun BLEConnStateIcon(bondedState: Int, connState: Int) {

    val state = BLEState.getBondedStateWithConnection(bondedState, connState)
    when (state) {
        BLEState.CONNECTED_BONDED -> {
            ConnectedLottie()
        }

        BLEState.CONNECTING_BONDING -> {
            LoadingLottie()
        }

        else -> {
            Image(
                painter = painterResource(id = R.drawable.ico_ble_off),
                contentDescription = "state off"
            )
        }
    }
}

@Composable
private fun ConnectedLottie() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_ble_on))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 1f)
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

@Composable
private fun LoadingLottie() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_ble_loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0.2f, 1f)
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun DeviceDetailContent(
    deviceViewModel: DeviceViewModel = viewModel(),
    naviController: NavController
) {
    val device = deviceViewModel.device.value
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
                    text = "Device Name: ${device?.name ?: "Unknown"}",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Device Address: ${device?.address}")

            }
        }

        HorizontalDivider(Modifier.padding(16.dp))

        if (deviceViewModel.isBonded.intValue == BLEState.BOND_BONDED &&
            deviceViewModel.isGattConnected.intValue == BLEState.GATT_SUCCESS
        ) {
            GattServiceList(
                HCBle.getGattServiceList(),
                naviController = naviController
            )
        }
    }
}

@Composable
fun GattServiceList(
    gattServiceList: List<BluetoothGattService>,
    naviController: NavController
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

                                    }
                            )
                            IconButton(
                                onClick = {
                                    HCBle.setCharacteristicUUID(characteristic.uuid.toString())
                                    naviController.navigate("characteristicDetail")
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