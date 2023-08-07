package com.jozu.weatherforecast.infrastructure.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 * Created by jozuko on 2023/07/11.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Serializable
data class ForecastApiModel(
    /** 気象台名 */
    @SerialName("publishingOffice") val publishingOffice: String?,

    /** 発表時刻 */
    @SerialName("reportDatetime") val reportDatetime: String?,

    @SerialName("timeSeries") val timeSeriesList: List<TimeSeriesApiModel>?,

    /** 降水量の７日間合計 */
    @SerialName("tempAverage") val tempAverage: AverageApiModel?,

    /** 降水量の７日間合計 */
    @SerialName("precipAverage") val precipAverage: AverageApiModel?,
) {
    companion object {
        const val TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}

@Serializable
data class TimeSeriesApiModel(
    @SerialName("timeDefines") val timeDefines: List<String>?,

    @SerialName("areas") val forecastAreas: List<ForecastAreaApiModel>?,
)

@Serializable
data class AverageApiModel(
    @SerialName("areas") val averageAreas: List<AverageAreaApiModel>?,
)

@Serializable
data class ForecastAreaApiModel(
    @SerialName("area") val area: AreaDataApiModel?,
    @SerialName("weatherCodes") val weatherCodes: List<String>?,
    @SerialName("weathers") val weathers: List<String>?,
    @SerialName("winds") val winds: List<String>?,
    /** 降水確率 */
    @SerialName("pops") val pops: List<String>?,
    /** 気温 */
    @SerialName("temps") val temps: List<String>?,
    /** 信頼度 */
    @SerialName("reliabilities") val reliabilities: List<String>?,
    @SerialName("tempsMin") val tempsMin: List<String>?,
    @SerialName("tempsMinUpper") val tempsMinUpper: List<String>?,
    @SerialName("tempsMinLower") val tempsMinLower: List<String>?,
    @SerialName("tempsMax") val tempsMax: List<String>?,
    @SerialName("tempsMaxUpper") val tempsMaxUpper: List<String>?,
    @SerialName("tempsMaxLower") val tempsMaxLower: List<String>?,
)

@Serializable
data class AreaDataApiModel(
    @SerialName("name") val name: String?,
    @SerialName("code") val code: String?,
)

@Serializable
data class AverageAreaApiModel(
    @SerialName("area") val area: AreaDataApiModel?,
    @SerialName("min") val min: String?,
    @SerialName("max") val max: String?,
)