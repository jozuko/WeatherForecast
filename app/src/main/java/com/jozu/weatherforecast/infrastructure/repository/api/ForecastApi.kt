package com.jozu.weatherforecast.infrastructure.repository.api

import com.jozu.weatherforecast.domain.repository.Area
import retrofit2.Response
import retrofit2.http.GET

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
interface ForecastApi {
    @GET("common/const/area.json")
    suspend fun getArea(): Response<Area>
}