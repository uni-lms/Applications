package ru.unilms.domain.settings.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.unilms.data.DataStore
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val store = DataStore(context)

}