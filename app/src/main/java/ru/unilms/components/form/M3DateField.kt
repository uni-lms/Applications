package ru.unilms.components.form

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import ru.unilms.utils.forms.Field
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import java.util.Calendar
import java.util.Date

class M3DateField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<Date?>,
    isVisible: Boolean = true,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: Date?) -> String)? = null,
    private val themeResId: Int = 0
) : Field<Date>(
    label = label,
    form = form,
    fieldState = fieldState,
    isVisible = isVisible,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter
) {
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!isVisible) {
            return
        }

        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        val year: Int
        val month: Int
        val day: Int

        val calendar = Calendar.getInstance()
        calendar.time = value.value ?: Date()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        val date = remember { mutableStateOf("") }
        val datePickerDialog = DatePickerDialog(
            context,
            themeResId,
            { _: DatePicker, yyyy: Int, mm: Int, dd: Int ->
                val c = Calendar.getInstance()
                c.set(yyyy, mm, dd, 0, 0)
                val d = c.time
                date.value = d.toString()
                value.value = d
                this.onChange(d, form)
            },
            year,
            month,
            day
        )

        datePickerDialog.setOnDismissListener {
            focusManager.clearFocus()
        }

        M3TextFieldComponent(
            modifier = modifier ?: Modifier,
            isEnabled = isEnabled,
            label = { Text(label, color = MaterialTheme.colorScheme.secondary) },
            text = formatter?.invoke(value.value) ?: value.value.toString(),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            isReadOnly = true,
            focusRequester = focusRequester,
            focusChanged = {
                if (it.isFocused) {
                    datePickerDialog.show()
                }
            }
        )
    }
}