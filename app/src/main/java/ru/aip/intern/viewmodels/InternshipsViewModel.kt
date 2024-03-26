package ru.aip.intern.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.aip.intern.domain.internships.data.Internship
import ru.aip.intern.domain.internships.service.InternshipsService
import ru.aip.intern.snackbar.SnackbarMessageHandler
import javax.inject.Inject

@HiltViewModel
class InternshipsViewModel @Inject constructor(
    private val snackbarMessageHandler: SnackbarMessageHandler,
    private val internshipsService: InternshipsService
) :
    ViewModel() {

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _internshipData = MutableLiveData(emptyList<Internship>())
    val internshipData: LiveData<List<Internship>> = _internshipData

    init {
        refresh()
    }

    fun refresh() {

        viewModelScope.launch {
            _isRefreshing.value = true
            val response = internshipsService.getEnrolled()

            if (response.isSuccess) {
                _internshipData.value = response.value!!
            } else {
                snackbarMessageHandler.postMessage(response.errorMessage!!)
            }

            _isRefreshing.value = false
        }
    }

}