package dev.tavarus.boardgamelogger.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class RemoteData<out T> {
    data class Failure(val error: Error): RemoteData<Nothing>()
    data object Loading : RemoteData<Nothing>()
    data class Success<T>(val data: T): RemoteData<T>()
}

fun <T> remoteCall( retries: Int = 3, call: suspend () -> T): Flow<RemoteData<T>> = flow {
    emit(RemoteData.Loading)
    var error: Error? = null
    for (i in 1..retries) {
        try {
            emit(RemoteData.Success(call()))
            error = null
            break
        } catch (e: Error) {
            error = e
            if (i < retries) {
                delay(1000L)
            }
        }
    }
    if (error != null) {
        emit(RemoteData.Failure(error))
    }

}