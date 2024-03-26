package ru.aip.intern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import ru.aip.intern.snackbar.SnackbarMessageHandler
import ru.aip.intern.ui.screens.AipApp
import ru.aip.intern.ui.theme.AltenarInternshipTheme
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @Inject
    lateinit var snackbarMessageHandler: SnackbarMessageHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Locale.setDefault(Locale("ru", "RU"))

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue with the operation
            } else {
                // Explain to the user that the feature is unavailable because the
                // features require a permission that the user has denied.
            }
        }

        setContent {
            AltenarInternshipTheme {
                AipApp(snackbarMessageHandler = snackbarMessageHandler)
            }
        }
    }

    fun getPermissionLauncher(): ActivityResultLauncher<String> {
        return requestPermissionLauncher
    }

}