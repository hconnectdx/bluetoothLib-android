package kr.co.hconnect.bluetoothlib_android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.hconnect.bluetoothlib.HCBle
import kr.co.hconnect.bluetoothlib.Testmy
import kr.co.hconnect.bluetoothlib_android.theme.Permissions
import kr.co.hconnect.myapplication123.ui.theme.MyApplication123Theme
import java.util.logging.Logger

class ExampleApps : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication123Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            PermissionManager.launchPermissions(Permissions.PERMISSION_SDK_31.toTypedArray())
                        } else {
                            PermissionManager.launchPermissions(Permissions.PERMISSION_SDK_30.toTypedArray())
                        }
                    }) {
                        Text("Check Permissions")
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
    MyApplication123Theme {
        Greeting("Android")
    }
}