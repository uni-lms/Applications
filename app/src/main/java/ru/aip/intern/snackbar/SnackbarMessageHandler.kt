package ru.aip.intern.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarMessageHandler @Inject constructor() {

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    suspend fun postMessage(message: String) {
        _message.emit(message)
    }

}