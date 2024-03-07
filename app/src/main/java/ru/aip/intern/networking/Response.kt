package ru.aip.intern.networking

sealed class Response<out T, out E> {
    data class Success<T>(val body: ResponseWrapper<T>) : Response<T, Nothing>()
    sealed class Error<E> : Response<Nothing, E>() {
        data class HttpError<E>(val code: Int, val errorBody: E?) : Error<E>()
        data object NetworkError : Error<Nothing>()
        data object SerializationError : Error<Nothing>()
    }
}