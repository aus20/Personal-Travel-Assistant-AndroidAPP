package com.travelassistant.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Exception? = null) : Result<Nothing>()
    object Loading : Result<Nothing>() // İsteğe bağlı olarak eklenebilir
}