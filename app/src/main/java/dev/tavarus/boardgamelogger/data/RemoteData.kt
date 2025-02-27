package dev.tavarus.boardgamelogger.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class RemoteData<out T> {
    data class Failure(val error: Error): RemoteData<Nothing>()
    data object Loading : RemoteData<Nothing>()
    data class Success<T>(val data: T): RemoteData<T>()
}

fun <T> remoteCall(call: suspend () -> T): Flow<RemoteData<T>> = flow {
    emit(RemoteData.Loading)
    try {
        emit(RemoteData.Success(call()))
    } catch (e: Error) {
        emit(RemoteData.Failure(e))
    }
}