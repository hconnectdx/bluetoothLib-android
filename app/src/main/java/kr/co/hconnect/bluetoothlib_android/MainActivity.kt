package kr.co.hconnect.bluetoothlib_android


import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib_android.navigation.NavigationComponent
import kr.co.hconnect.permissionlib.PermissionManager
import kr.co.hconnect.myapplication123.ui.theme.MyApplicationTheme

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationComponent()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStart() {
        super.onStart()

        // 퍼미션 런처 등록
        PermissionManager.registerPermissionLauncher(this)

        // BLE 초기화
        HCBle.init(this)
    }
}
