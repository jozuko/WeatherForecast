package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.adapter.ForecastAdapter
import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.Office
import com.jozu.weatherforecast.infrastructure.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class GetForecastUseCaseImpl @Inject constructor(
    private val forecastRepository: ForecastRepository,
) : GetForecastUseCase {
    override fun invoke(office: Office): Flow<Future<Forecast>> {
        return forecastRepository.getForecast(officeId = office.id).map { apiModelFuture ->
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