package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.Office
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
interface GetForecastUseCase {
    operator fun invoke(office: Office): Flow<Future<Forecast>>
}