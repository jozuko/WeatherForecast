package com.jozu.weatherforecast.infrastructure.repository

import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.AreaRepository
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.adapter.AreaAdapter
import com.jozu.weatherforecast.infrastructure.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.api.apiFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class AreaRepositoryImpl @Inject constructor(
    private val forecastApi: ForecastApi,
    private val dispatchers: CoroutineDispatcher,
) : AreaRepository {
    override fun getArea(): Flow<Future<Area>> {
        return apiFlow(dispatchers) {
            forecastApi.getArea()
        }.map { apiModelFuture ->
            when (apiModelFuture) {
                is Future.Error -> Future.Error(apiModelFuture.error)
                is Future.Success -> Future.Success(AreaAdapter.adaptFromApi(apiModelFuture.value))
                is Future.Proceeding -> Future.Idle
                is Future.Idle -> Future.Idle
            }
        }
    }
}