package ru.aip.intern.ui.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RequestingNotificationPermissionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(Icons.AutoMirrored.Outlined.Message, contentDescription = null)
        },
        title = {
            Text(text = "Отправка уведомлений")
        },
        text = {
            Text(text = "Приложение может отправлять уведомления о предстоящих событиях. Нажмите разрешить, чтобы предоставить ему доступ к отправке уведомлений")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Разрешить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text("Запретить")
            }
        }
    )
}