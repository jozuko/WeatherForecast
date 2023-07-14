package com.jozu.weatherforecast.infrastructure.repository.api

import com.jozu.weatherforecast.domain.Future
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import retrofit2.Response

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
inline fun <reified T : Any> apiFlow(ioDispatcher: CoroutineDispatcher, crossinline call: suspend () -> Response<T>): Flow<Future<T>> =
    flow<Future<T>> {
        val response = call()
        if (response.isSuccessful) {
            emit(Future.Success(value = response.body()!!))
        } else {
            throw HttpException(response)
        }
    }.catch { cause ->
        emit(Future.Error(cause))
    }.onStart {
        emit(Future.Proceeding)
    }.flowOn(ioDispatcher)

inline fun <reified T : Any?> apiNullableFlow(ioDispatcher: CoroutineDispatcher, crossinline call: suspend () -> Response<T?>): Flow<Future<T?>> =
    flow<Future<T?>> {
        val response = call()
        if (response.isSuccessful) {
            emit(Future.Success(value = response.body()))
        } else {
            throw HttpException(response)
        }
    }.catch { cause ->
        emit(Future.Error(cause))
    }.onStart {
        emit(Future.Proceeding)
    }.flowOn(ioDispatcher)

