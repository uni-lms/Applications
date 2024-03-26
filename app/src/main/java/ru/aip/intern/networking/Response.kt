package ru.aip.intern.networking

data class Response<T>(val isSuccess: Boolean, val value: T?, val errorMessage: String?)