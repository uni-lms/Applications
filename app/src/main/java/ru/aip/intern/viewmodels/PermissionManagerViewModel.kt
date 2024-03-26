package ru.aip.intern.viewmodels

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.aip.intern.MainActivity
import ru.aip.intern.permissions.PermissionStatus
import ru.aip.intern.storage.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class PermissionManagerViewModel @Inject constructor(val storage: DataStoreRepository) :
    ViewModel() {

    fun requestPermission(context: Context, permission: String) {
        val activity = context.findActivity()
        (activity as? MainActivity)?.getPermissionLauncher()?.launch(permission)
    }

    fun checkPermission(context: Context, permission: String): PermissionStatus {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            permission
        )

        return if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
    }

}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}