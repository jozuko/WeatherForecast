package com.jozu.weatherforecast.adapter

import com.jozu.weatherforecast.domain.AreaForecast
import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.ForecastDataType
import com.jozu.weatherforecast.domain.TimeData
import com.jozu.weatherforecast.domain.extension.CalendarEx
import com.jozu.weatherforecast.domain.repository.ForecastApiModel
import com.jozu.weatherforecast.domain.repository.ForecastAreaApiModel
import com.jozu.weatherforecast.domain.repository.TimeSeriesApiModel

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
object ForecastAdapter {
    fun adaptFromApi(apiModelList: List<ForecastApiModel>): Forecast {
        val timeSeriesList = apiModelList.map { it.timeSeriesList ?: emptyList() }.flatten()
        val firstApiModel = apiModelList.firstOrNull()

        return Forecast(
            publishingOffice = firstApiModel?.publishingOffice ?: "",
            reportDatetime = firstApiModel?.reportDatetime?.let { CalendarEx.parse(ForecastApiModel.TIME_FORMAT, it) },
            weatherCodeList = getAreaForecastList(ForecastDataType.WEATHER_CODE, timeSeriesList),
            weatherList = getAreaForecastList(ForecastDataType.WEATHER, timeSeriesList),
            windList = getAreaForecastList(ForecastDataType.WIND, timeSeriesList),
            rainyPercentList = getAreaForecastList(ForecastDataType.RAINY, timeSeriesList),
            temperatureList = getAreaForecastList(ForecastDataType.TEMPERATURE, timeSeriesList),
            weekWeatherList = getAreaForecastList(ForecastDataType.WEEK_WEATHER, timeSeriesList),
            weekRainyList = getAreaForecastList(ForecastDataType.WEEK_RAINY, timeSeriesList),
            weekTemperatureList = getAreaForecastList(ForecastDataType.WEEK_TEMPERATURE, timeSeriesList),
        )
    }

    private fun getAreaForecastList(forecastDataType: ForecastDataType, timeSeriesList: List<TimeSeriesApiModel>): List<AreaForecast> {
        val targetSeries = getTimeSeries(forecastDataType, timeSeriesList) ?: return emptyList()

        return targetSeries.forecastAreas?.map { areaApiModel ->
            AreaForecast(
                areaName = areaApiModel.area?.name ?: "",
                areaCode = areaApiModel.area?.code ?: "",
                forecastDataType = forecastDataType,
                dataList = getTimeDataList(forecastDataType, targetSeries, areaApiModel),
            )
        } ?: emptyList()
    }

    private fun getTimeDataList(forecastDataType: ForecastDataType, targetSeries: TimeSeriesApiModel, areaApiModel: ForecastAreaApiModel): List<TimeData> {
        val timeDefines = targetSeries.timeDefines?.map { CalendarEx.parse(ForecastApiModel.TIME_FORMAT, it) } ?: emptyList()

        val dataList = when (forecastDataType) {
            ForecastDataType.WEATHER_CODE -> areaApiModel.weatherCodes
            ForecastDataType.WEATHER -> areaApiModel.weathers
            ForecastDataType.WIND -> areaApiModel.winds
            ForecastDataType.RAINY -> areaApiModel.pops
            ForecastDataType.TEMPERATURE -> areaApiModel.temps
            ForecastDataType.WEEK_WEATHER -> areaApiModel.weatherCodes
            ForecastDataType.WEEK_RAINY -> areaApiModel.pops
            ForecastDataType.WEEK_TEMPERATURE -> areaApiModel.tempsMin?.mapIndexed { index, tempMin -> "$tempMin/${areaApiModel.tempsMax?.get(index)}" }
        } ?: emptyList()

        return timeDefines.mapIndexedNotNull { index, time ->
            time ?: return@mapIndexedNotNull null
            TimeData(
                time = time,
                data = dataList[index],
            )
        }
    }

    private fun getTimeSeries(forecastDataType: ForecastDataType, timeSeriesList: List<TimeSeriesApiModel>): TimeSeriesApiModel? {
        return when (forecastDataType) {
            ForecastDataType.WEATHER_CODE, ForecastDataType.WEATHER, ForecastDataType.WIND -> {
                timeSeriesList.firstOrNull { timeSeries ->
                    timeSeries.forecastAreas?.find { !it.weatherCodes.isNullOrEmpty() && it.reliabilities.isNullOrEmpty() } != null
                }
            }

            ForecastDataType.RAINY -> {
                timeSeriesList.firstOrNull { timeSeries ->
                    timeSeries.forecastAreas?.find { !it.pops.isNullOrEmpty() && it.reliabilities.isNullOrEmpty() } != null
                }
            }

            ForecastDataType.TEMPERATURE -> {
                timeSeriesList.firstOrNull { timeSeries ->
                    timeSeries.forecastAreas?.find { !it.temps.isNullOrEmpty() && it.reliabilities.isNullOrEmpty() } != null
                }
            }

            ForecastDataType.WEEK_WEATHER -> {
                timeSeriesList.firstOrNull { timeSeries ->
                    timeSeries.forecastAreas?.find { !it.weatherCodes.isNullOrEmpty() && !it.reliabilities.isNullOrEmpty() } != null
                }
            }

            ForecastDataType.WEEK_RAINY -> {
                timeSeriesList.firstOrNull { timeSeries ->
                    timeSeries.forecastAreas?.find { !it.pops.isNullOrEmpty() && !it.reliabilities.isNullOrEmpty() } != null
                }
            }

            ForecastDataType.WEEK_TEMPERATURE -> {
                timeSeriesList.firstOrNull { timeSeries ->
                    timeSeries.forecastAreas?.find { !it.tempsMin.isNullOrEmpty() && !it.tempsMax.isNullOrEmpty() } != null
                }
            }
        }
    }
}