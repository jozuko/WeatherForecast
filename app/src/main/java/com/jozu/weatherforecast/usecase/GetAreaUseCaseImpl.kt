package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.adapter.AreaAdapter
import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.infrastructure.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class GetAreaUseCaseImpl @Inject constructor(
    private val forecastRepository: ForecastRepository,
) : GetAreaUseCase {
    override fun invoke(): Flow<Future<Area>> {
        return forecastRepository.getArea().map { apiModelFuture ->
            when (apiModelFuture) {
                is Future.Error -> Future.Error(apiModelFuture.error)
                is Future.Success -> Future.Success(AreaAdapter.adaptFromApi(apiModelFuture.value))
                is Future.Proceeding -> Future.Idle
                is Future.Idle -> Future.Idle
            }
        }
    }
}