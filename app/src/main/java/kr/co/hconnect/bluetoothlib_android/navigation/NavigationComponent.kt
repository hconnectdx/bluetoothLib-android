package kr.co.hconnect.bluetoothlib_android.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib_android.model.Permissions
import kr.co.hconnect.bluetoothlib_android.view.DeviceDetailScreen
import kr.co.hconnect.bluetoothlib_android.view.characteristic_detail.CharacteristicDetail
import kr.co.hconnect.bluetoothlib_android.view.main.HomeScreen
import kr.co.hconnect.bluetoothlib_android.viewmodel.BondedDevicesViewModel
import kr.co.hconnect.bluetoothlib_android.viewmodel.DeviceViewModel
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanListViewModel
import kr.co.hconnect.permissionlib.PermissionManager

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val scanViewModel = viewModel<ScanListViewModel>()
    val deviceViewModel = viewModel<DeviceViewModel>()
    val bondedDevicesViewModel = viewModel<BondedDevicesViewModel>()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (PermissionManager.isGrantedPermissions(context, Permissions.PERMISSION_SDK_31)) {
            bondedDevicesViewModel.bondedDevices.value = HCBle.getBondedDevices()
        } else {
            // 권한이 부여되지 않은 경우 권한 요청
            PermissionManager.launchPermissions(Permissions.PERMISSION_SDK_31) { grantedPermissions ->
                if (grantedPermissions.all { it.value }) {
                    bondedDevicesViewModel.bondedDevices.value = HCBle.getBondedDevices()
                } else {

                }
            }
        }
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