package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.ForecastRepository
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.Office
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class GetForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository,
) {
    operator fun invoke(office: Office): Flow<Future<Forecast>> {
        return forecastRepository.getForecast(officeId = office.code)
    }
}