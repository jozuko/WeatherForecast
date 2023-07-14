package com.jozu.weatherforecast.infrastructure.repository

import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.repository.AreaApiModel
import com.jozu.weatherforecast.domain.repository.ForecastApiModel
import com.jozu.weatherforecast.infrastructure.repository.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.repository.api.apiFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class ForecastRepository constructor(
    private val forecastApi: ForecastApi,
    private val dispatchers: CoroutineDispatcher,
) {
    fun getArea(): Flow<Future<AreaApiModel>> {
        return apiFlow(dispatchers) { forecastApi.getArea() }
    }

    fun getForecast(officeId: String): Flow<Future<List<ForecastApiModel>>> {
        return apiFlow(dispatchers) { forecastApi.getForecast(officeId = officeId) }
    }
}