package kr.co.hconnect.bluetoothlib_android.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib_android.view.DeviceDetailScreen
import kr.co.hconnect.bluetoothlib_android.view.characteristic_detail.CharacteristicDetail
import kr.co.hconnect.bluetoothlib_android.view.main.HomeScreen
import kr.co.hconnect.bluetoothlib_android.viewmodel.BondedDevicesViewModel
import kr.co.hconnect.bluetoothlib_android.viewmodel.DeviceViewModel
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanListViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val scanViewModel = viewModel<ScanListViewModel>()
    val deviceViewModel = viewModel<DeviceViewModel>()
    val bondedDevicesViewModel = viewModel<BondedDevicesViewModel>()

    LaunchedEffect(key1 = Unit) {
        bondedDevicesViewModel.bondedDevices.value = HCBle.getBondedDevices()
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                scanViewModel = scanViewModel,
                deviceViewModel = deviceViewModel,
                bondedDevicesViewModel = bondedDevicesViewModel
            )
        }
        composable("detail") {
            DeviceDetailScreen(
                navController = navController,
                deviceViewModel = deviceViewModel
            )
        }
        composable("characteristicDetail") {
            CharacteristicDetail(navController)
        }
    }
}