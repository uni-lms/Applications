package ru.aip.intern.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.aip.intern.domain.content.assignment.data.AssignmentInfo
import ru.aip.intern.domain.content.assignment.service.AssignmentService
import ru.aip.intern.snackbar.SnackbarMessageHandler
import java.time.LocalDateTime
import java.util.UUID

@HiltViewModel(assistedFactory = AssignmentViewModel.Factory::class)
class AssignmentViewModel @AssistedInject constructor(
    private val snackbarMessageHandler: SnackbarMessageHandler,
    private val assignmentService: AssignmentService,
    @Assisted private val id: UUID
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: UUID): AssignmentViewModel
    }

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    val defaultContent = AssignmentInfo(
        id = id,
        title = "",
        deadline = LocalDateTime.now(),
        description = "",
        fileId = id
    )

    private val _assignmentData = MutableLiveData(defaultContent)
    val assignmentData: LiveData<AssignmentInfo> = _assignmentData

    init {
        refresh()
    }

    fun refresh() {

        viewModelScope.launch {
            _isRefreshing.value = true
            val response = assignmentService.getInfo(id)

            if (response.isSuccess) {
                _assignmentData.value = response.value!!
            } else {
                snackbarMessageHandler.postMessage(response.errorMessage!!)
            }

            _isRefreshing.value = false
        }
    }
}