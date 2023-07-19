package com.jozu.weatherforecast.infrastructure.api

import com.jozu.weatherforecast.infrastructure.api.model.AreaApiModel
import com.jozu.weatherforecast.infrastructure.api.model.ForecastApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
interface ForecastApi {
    @GET("common/const/area.json")
    suspend fun getArea(): Response<AreaApiModel>

    @GET("forecast/data/forecast/{office-id}.json")
    suspend fun getForecast(
        @Path("office-id") officeId: String,
    ): Response<List<ForecastApiModel>>
}