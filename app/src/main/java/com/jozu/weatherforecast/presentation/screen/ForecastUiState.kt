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
data class ForecastUiState(
    val areaFuture: Future<Area> = Future.Idle,
    val center: Center? = null,
    val office: Office? = null,
)