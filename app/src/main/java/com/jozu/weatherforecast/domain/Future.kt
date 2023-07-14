package com.jozu.weatherforecast.domain

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
sealed class Future<out T> {
    object Idle : Future<Nothing>()
    object Proceeding : Future<Nothing>()
    data class Success<out T>(val value: T) : Future<T>()
    data class Error(val error: Throwable) : Future<Nothing>()
}