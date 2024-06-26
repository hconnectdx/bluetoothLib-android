package kr.co.hconnect.permissionlib

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

object PermissionManager {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var resultCallback: (permissions: Map<String, Boolean>) -> Unit

    fun registerPermissionLauncher(
        activity: AppCompatActivity
    ) {
        permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val permissionMap = mapOf<String, Boolean>()
                permissions.forEach { p ->
                    if (p.value) {
                        Log.d("PermissionManager", "Permission Granted: ${p.key}")
                        permissionMap.plus(Pair(p.key, true))
                    } else {
                        Log.e("PermissionManager", "Permission Denied: ${p.key}")
                    }
                }
                resultCallback(permissions)
            }
    }

    fun launchPermissions(
        permissions: Array<String>,
        resultCallback: (permissions: Map<String, Boolean>) -> Unit
    ) {
        this.resultCallback = resultCallback
        permissionLauncher.launch(permissions)
    }
}