package com.jozu.weatherforecast.domain.repository

import com.google.gson.annotations.SerializedName

/**
 *
 * Created by jozuko on 2023/07/11.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
data class ForecastApiModel(
    /** 気象台名 */
    @SerializedName("publishingOffice") val publishingOffice: String?,

    /** 発表時刻 */
    @SerializedName("reportDatetime") val reportDatetime: String?,

    @SerializedName("timeSeries") val timeSeriesList: List<TimeSeriesApiModel>?,

    /** 降水量の７日間合計 */
    @SerializedName("tempAverage") val tempAverage: AverageApiModel?,

    /** 降水量の７日間合計 */
    @SerializedName("precipAverage") val precipAverage: AverageApiModel?,
) {
    companion object {
        const val TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}

data class TimeSeriesApiModel(
    @SerializedName("timeDefines") val timeDefines: List<String>?,

    @SerializedName("areas") val forecastAreas: List<ForecastAreaApiModel>?,
)

data class AverageApiModel(
    @SerializedName("areas") val averageAreas: List<AverageAreaApiModel>?,
)

data class ForecastAreaApiModel(
    @SerializedName("area") val area: AreaDataApiModel?,
    @SerializedName("weatherCodes") val weatherCodes: List<String>?,
    @SerializedName("weathers") val weathers: List<String>?,
    @SerializedName("winds") val winds: List<String>?,
    /** 降水確率 */
    @SerializedName("pops") val pops: List<String>?,
    /** 気温 */
    @SerializedName("temps") val temps: List<String>?,
    /** 信頼度 */
    @SerializedName("reliabilities") val reliabilities: List<String>?,
    @SerializedName("tempsMin") val tempsMin: List<String>?,
    @SerializedName("tempsMinUpper") val tempsMinUpper: List<String>?,
    @SerializedName("tempsMinLower") val tempsMinLower: List<String>?,
    @SerializedName("tempsMax") val tempsMax: List<String>?,
    @SerializedName("tempsMaxUpper") val tempsMaxUpper: List<String>?,
    @SerializedName("tempsMaxLower") val tempsMaxLower: List<String>?,
)

data class AreaDataApiModel(
    @SerializedName("name") val name: String?,
    @SerializedName("code") val code: String?,
)

data class AverageAreaApiModel(
    @SerializedName("area") val area: AreaDataApiModel?,
    @SerializedName("min") val min: String?,
    @SerializedName("max") val max: String?,
)