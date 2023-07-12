package com.jozu.weatherforecast.domain

import java.util.Calendar

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
data class Forecast(
    val publishingOffice: String,
    val reportDatetime: Calendar?,
    val weatherCodeList: List<AreaForecast>,
    val weatherList: List<AreaForecast>,
    val windList: List<AreaForecast>,
    val rainyPercentList: List<AreaForecast>,
    val temperatureList: List<AreaForecast>,
    val weekWeatherList: List<AreaForecast>,
    val weekRainyList: List<AreaForecast>,
    val weekTemperatureList: List<AreaForecast>,
)

enum class ForecastDataType {
    WEATHER_CODE, WEATHER, WIND, RAINY, TEMPERATURE, WEEK_WEATHER, WEEK_RAINY, WEEK_TEMPERATURE,
}

data class AreaForecast(
    val areaName: String,
    val areaCode: String,
    val forecastDataType: ForecastDataType,
    val dataList: List<TimeData>,
)

data class TimeData(
    val time: Calendar,
    val data: String,
)
