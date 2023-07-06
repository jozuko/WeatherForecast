package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.repository.Area
import com.jozu.weatherforecast.infrastructure.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class GetAreaUseCaseImpl @Inject constructor(
    private val forecastRepository: ForecastRepository,
) : GetAreaUseCase {
    override suspend fun invoke(): Flow<Future<Area>> {
        return forecastRepository.getArea()
    }
}