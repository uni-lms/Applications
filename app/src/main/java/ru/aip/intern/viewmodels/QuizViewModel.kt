package ru.aip.intern.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.aip.intern.snackbar.SnackbarMessageHandler
import java.util.UUID

@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
class QuizViewModel @AssistedInject constructor(
    snackbarMessageHandler: SnackbarMessageHandler,
    @Assisted id: UUID
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: UUID): QuizViewModel
    }

    var isRefreshing = MutableStateFlow(false)
        private set

    var data = MutableStateFlow(false)
        private set

    init {
        refresh(id)
    }

    fun refresh(id: UUID) {
        viewModelScope.launch {

        }
    }

}