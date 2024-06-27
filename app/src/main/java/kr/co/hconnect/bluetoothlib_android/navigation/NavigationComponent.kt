package kr.co.hconnect.bluetoothlib_android.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.co.hconnect.bluetoothlib_android.view.DeviceDetailScreen
import kr.co.hconnect.bluetoothlib_android.view.main.HomeScreen
import kr.co.hconnect.bluetoothlib_android.viewmodel.ScanListViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val scanViewModel = ScanListViewModel()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, scanViewModel)
        }
        composable("detail") {
            DeviceDetailScreen(navController, scanViewModel)
        }
    }
}