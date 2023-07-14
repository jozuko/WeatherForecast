package com.jozu.weatherforecast.presentation.screen

import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Center
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.Office

/**
 *
 * Created by jozuko on 2023/07/14.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class ForecastViewModelState(
    val areaFuture: Future<Area> = Future.Idle,
    val center: Center? = null,
    val office: Office? = null,
) {
    fun toUiState(): ForecastUiState {
        when (areaFuture) {
            is Future.Idle,
            is Future.Proceeding,
            is Future.Error -> {
                return ForecastUiState(
                    areaFuture = areaFuture,
                    center = null,
                    office = null,
                )
            }

            is Future.Success -> {
                val center = this.center ?: areaFuture.value.centers.firstOrNull()

                return ForecastUiState(
                    areaFuture = areaFuture,
                    center = center,
                    office = this.office ?: center?.offices?.firstOrNull()
                )
            }
        }
    }
}