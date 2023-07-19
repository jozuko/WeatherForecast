package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.AreaRepository
import com.jozu.weatherforecast.domain.Future
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class GetAreaUseCase @Inject constructor(
    private val areaRepository: AreaRepository,
) {
    operator fun invoke(): Flow<Future<Area>> {
        return areaRepository.getArea()
    }
}