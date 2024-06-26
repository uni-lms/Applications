package ru.aip.intern.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.aip.intern.R
import ru.aip.intern.domain.content.quiz.service.QuizService
import ru.aip.intern.domain.internships.service.InternshipsService
import ru.aip.intern.snackbar.SnackbarMessageHandler
import ru.aip.intern.ui.managers.TitleManager
import ru.aip.intern.ui.state.QuizState
import ru.aip.intern.util.UiText
import java.util.UUID

@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
class QuizViewModel @AssistedInject constructor(
    private val snackbarMessageHandler: SnackbarMessageHandler,
    private val titleManager: TitleManager,
    private val quizService: QuizService,
    private val internshipsService: InternshipsService,
    @Assisted private val id: UUID
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: UUID): QuizViewModel
    }

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            titleManager.update(UiText.StringResource(R.string.quiz))
        }

        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }

            val response = quizService.getQuizInfo(id)

            if (response.isSuccess) {
                _state.update {
                    it.copy(
                        quizInfo = response.value!!
                    )
                }

                titleManager.update(UiText.DynamicText(response.value!!.title))

            } else {
                snackbarMessageHandler.postMessage(response.errorMessage!!)
            }
            val roleResponse = internshipsService.getRole(response.value!!.internshipId)

            if (roleResponse.isSuccess) {
                _state.update {
                    it.copy(
                        role = roleResponse.value!!.name
                    )
                }

            } else {
                snackbarMessageHandler.postMessage(roleResponse.errorMessage!!)
            }

            _state.update {
                it.copy(
                    isRefreshing = false
                )
            }
        }
    }

    fun toggleDialog() {
        _state.update {
            it.copy(
                isStartAttemptDialogVisible = !it.isStartAttemptDialogVisible
            )
        }
    }

    fun startAttempt(quizId: UUID, afterSuccess: (UUID) -> Unit) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }

            val response = quizService.startAttempt(quizId)

            if (response.isSuccess) {
                afterSuccess(response.value!!.id)
            } else {
                snackbarMessageHandler.postMessage(response.errorMessage!!)
            }

            _state.update {
                it.copy(
                    isRefreshing = false
                )
            }

        }
    }

}