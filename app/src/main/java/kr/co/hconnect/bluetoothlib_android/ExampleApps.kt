package kr.co.hconnect.bluetoothlib_android

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.permissionlib.PermissionManager
import kr.co.hconnect.myapplication123.ui.theme.MyApplicationTheme

class ExampleApps : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("블루투스 테스트 앱")
                    Button(onClick = {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                            PermissionManager.launchPermissions(Permissions.PERMISSION_SDK_31.toTypedArray())
//                        } else {
//                            PermissionManager.launchPermissions(Permissions.PERMISSION_SDK_30.toTypedArray())
//                        }
                        HCBle.init(this@ExampleApps)
                    }) {
                        Text("Scan Bluetooth Devices")
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStart() {
        super.onStart()
        PermissionManager.registerPermissionLauncher(this) { permissions ->
            permissions.forEach { p ->
                if (p.value) {
                    Log.d("PermissionManager", "Permission Granted.")
                } else {
                    Log.e("PermissionManager", "Permission Denied.")
                }
            }
        }
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