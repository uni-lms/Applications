package ru.unilms.domain.common.form

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import java.util.*

abstract class Field<T> constructor(
    open val fieldState: FieldState<T?>,
    open val label: String,
    open val form: Form,
    open val keyboardType: KeyboardType = KeyboardType.Text,
    open val keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    open val visualTransformation: VisualTransformation = VisualTransformation.None,
    open val isVisible: Boolean = true,
    open val isEnabled: Boolean = true,
    open val modifier: Modifier? = Modifier,
    open val imeAction: ImeAction? = ImeAction.Next,
    open val formatter: ((raw: T?) -> String)? = null,
    var changed: ((v: T?) -> Unit)? = null
) {
    var value: MutableState<T?> = mutableStateOf(null)

    /**
     * This method is called when the value on the input field is changed
     */
    fun onChange(v: Any?, form: Form) {
        @Suppress("UNCHECKED_CAST")
        this.value.value = v as T?
        this.updateFormValue()
        form.validate()
        changed?.invoke(v)
    }

    fun updateComposableValue() {
        this.value.value = fieldState.state.value
    }

    private fun updateFormValue() {
        fieldState.state.value = this.value.value
        fieldState.hasChanges.value = true
    }

    @SuppressLint("NotConstructor")
    @Composable
    abstract fun Field()
}