package ru.aip.intern.viewmodels

import android.app.DownloadManager
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.launch
import ru.aip.intern.auth.AuthManager
import ru.aip.intern.domain.content.file.data.FileInfo
import ru.aip.intern.domain.content.file.service.FileService
import ru.aip.intern.networking.HttpClientFactory
import ru.aip.intern.snackbar.SnackbarMessageHandler
import java.util.UUID

@HiltViewModel(assistedFactory = FileViewModel.Factory::class)
class FileViewModel @AssistedInject constructor(
    private val snackbarMessageHandler: SnackbarMessageHandler,
    private val fileService: FileService,
    private val downloadManager: DownloadManager,
    private val authManager: AuthManager,
    @Assisted private val id: UUID
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: UUID): FileViewModel
    }

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    val defaultContent = FileInfo(
        title = "",
        fileName = "",
        fileSize = "",
        contentType = "",
        extension = "",
        id = id
    )

    private val _fileData = MutableLiveData(defaultContent)
    val fileData: LiveData<FileInfo> = _fileData

    init {
        refresh()
    }

    fun refresh() {

        viewModelScope.launch {
            _isRefreshing.value = true
            val response = fileService.getFileInfo(id)

            if (response.isSuccess) {
                _fileData.value = response.value!!
            } else {
                snackbarMessageHandler.postMessage(response.errorMessage!!)
            }

            _isRefreshing.value = false
        }
    }

    private fun buildDownloadUrl(): String {
        val url = "https://${HttpClientFactory.baseUrl}/content/file/${id}/download"
        Log.d("downloadUrl", url)
        return url
    }

    fun downloadFile() {
        viewModelScope.launch {
            val request = DownloadManager.Request(buildDownloadUrl().toUri())
                .setMimeType(fileData.value?.contentType)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileData.value?.fileName)
                .addRequestHeader(HttpHeaders.Authorization, authManager.getAuthHeaderValue())
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "AipDownloads/${fileData.value?.fileName}"
                )

            downloadManager.enqueue(request)
        }
    }


}