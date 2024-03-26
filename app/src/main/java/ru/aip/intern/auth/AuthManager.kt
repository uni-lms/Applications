package ru.aip.intern.auth

import kotlinx.coroutines.flow.first
import ru.aip.intern.storage.DataStoreRepository
import javax.inject.Inject

class AuthManager @Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    private suspend fun getCurrentToken(): String {
        return dataStoreRepository.apiKey.first() ?: ""
    }

    suspend fun getAuthHeaderValue(): String {
        return "Bearer ${getCurrentToken()}"
    }

}