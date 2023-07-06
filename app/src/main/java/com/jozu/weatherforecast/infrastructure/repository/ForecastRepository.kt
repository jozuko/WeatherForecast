package com.jozu.weatherforecast.infrastructure.repository

import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.repository.Area
import com.jozu.weatherforecast.infrastructure.repository.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.repository.api.apiFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Singleton
class ForecastRepository @Inject constructor(
    private val forecastApi: ForecastApi,
) {
    fun getArea(): Flow<Future<Area>> = apiFlow { forecastApi.getArea() }
}