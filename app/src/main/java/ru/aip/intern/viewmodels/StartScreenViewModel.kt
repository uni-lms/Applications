package ru.aip.intern.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.aip.intern.domain.auth.service.AuthService
import ru.aip.intern.navigation.Screen
import ru.aip.intern.snackbar.SnackbarMessageHandler
import ru.aip.intern.storage.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authService: AuthService,
    private val snackbarMessageHandler: SnackbarMessageHandler,
) :
    ViewModel() {

    private val _startScreen = MutableLiveData<Screen>()
    val startScreen: LiveData<Screen> = _startScreen

    init {
        viewModelScope.launch {
            // Use a coroutine to fetch both apiKey and startScreenName concurrently
            val apiKey =
                async { dataStoreRepository.apiKey.first() }

            // Await both values
            var apiKeyValue = apiKey.await()

            val response = authService.whoami()

            if (!response.isSuccess) {
                apiKeyValue = ""
                snackbarMessageHandler.postMessage("Истёк срок действия токена")
            }

            // Determine the start screen based on the fetched values
            val determinedScreen = when (apiKeyValue) {
                "", null -> Screen.Login
                else -> Screen.Internships
            }

            // Assign the determined start screen to _startScreen
            _startScreen.value = determinedScreen
        }
    }

    fun updateStartScreen(value: Screen) {
        _startScreen.value = value
    }
}