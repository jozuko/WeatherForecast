package com.jozu.weatherforecast.infrastructure.repository

import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.ForecastRepository
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.adapter.ForecastAdapter
import com.jozu.weatherforecast.infrastructure.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.api.apiFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class ForecastRepositoryImpl constructor(
    private val forecastApi: ForecastApi,
    private val dispatchers: CoroutineDispatcher,
) : ForecastRepository {
    override fun getForecast(officeId: String): Flow<Future<Forecast>> {
        return apiFlow(dispatchers) {
            forecastApi.getForecast(officeId = officeId)
        }.map { apiModelFuture ->
            when (apiModelFuture) {
                is Future.Error -> {
                    Future.Error(apiModelFuture.error)
                }

                is Future.Success -> {
                    Future.Success(ForecastAdapter.adaptFromApi(apiModelFuture.value))
                }

                is Future.Proceeding -> {
                    Future.Proceeding
                }

                else -> {
                    Future.Idle
                }
            }
        }
    }
}