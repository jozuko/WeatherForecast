package com.jozu.weatherforecast.domain

import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
interface ForecastRepository {
    fun getForecast(officeId: String): Flow<Future<Forecast>>
}