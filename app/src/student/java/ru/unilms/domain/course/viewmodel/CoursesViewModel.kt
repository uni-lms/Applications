package ru.unilms.domain.course.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.unilms.data.DataStore
import ru.unilms.domain.common.network.HttpClientFactory
import ru.unilms.domain.common.network.processResponse
import ru.unilms.domain.course.model.Course
import ru.unilms.domain.course.network.CoursesServiceImpl
import ru.unilms.domain.course.util.enums.CourseType
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    var isLoading = false
    private var store: DataStore = DataStore(context)
    private lateinit var service: CoursesServiceImpl

    init {
        viewModelScope.launch {
            store.apiUri.collect {
                HttpClientFactory.baseUrl = it
            }
        }

        viewModelScope.launch {
            store.token.collect {
                service = CoursesServiceImpl(it)
            }
        }
    }

    suspend fun loadCourses(coursesType: CourseType = CourseType.Current): List<Course> {
        var result: List<Course>? = null

        val response = service.getEnrolled(coursesType)

        viewModelScope.launch {
            isLoading = true
            coroutineScope {
                result = processResponse(response)
            }
            isLoading = false
        }

        return result ?: emptyList()
    }

}
