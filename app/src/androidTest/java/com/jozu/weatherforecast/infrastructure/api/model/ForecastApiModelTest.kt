package com.jozu.weatherforecast.infrastructure.api.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jozu.weatherforecast.domain.extension.CalendarEx
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStreamReader

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Suppress("LocalVariableName")
@RunWith(AndroidJUnit4::class)
class ForecastApiModelTest {
    @Test
    fun readJson() {
        val androidTestContext = InstrumentationRegistry.getInstrumentation().context
        InputStreamReader(androidTestContext.assets.open("forecast.json")).use { inputStreamReader ->
            val typeToken = object : TypeToken<List<ForecastApiModel>>() {}.type
            val forecastList: List<ForecastApiModel> = Gson().fromJson(inputStreamReader, typeToken)
            assert(forecastList.count() == 2)

            val forecast1 = forecastList[0]
            assertEquals("宇都宮地方気象台", forecast1.publishingOffice)
            assertEquals("2023-07-12T05:00:00+09:00", forecast1.reportDatetime)
            val reportTime = CalendarEx.parse("yyyy-MM-dd'T'HH:mm:ssZ", forecast1.reportDatetime!!)
            assertEquals(1689105600000L, reportTime?.timeInMillis)
            assertEquals(3, forecast1.timeSeriesList!!.count())

            val timeSeries1_1 = forecast1.timeSeriesList!![0]
            assertEquals("2023-07-12T05:00:00+09:00", timeSeries1_1.timeDefines!![0])
            assertEquals("2023-07-13T00:00:00+09:00", timeSeries1_1.timeDefines!![1])
            assertEquals(2, timeSeries1_1.forecastAreas!!.count())
            assertEquals("南部", timeSeries1_1.forecastAreas!![0].area!!.name)
            assertEquals("090010", timeSeries1_1.forecastAreas!![0].area!!.code)
            assertEquals("112", timeSeries1_1.forecastAreas!![0].weatherCodes!![0])
            assertEquals("200", timeSeries1_1.forecastAreas!![0].weatherCodes!![1])
            assertEquals("晴れ　後　くもり　夕方　一時　雨　所により　夕方　から　夜のはじめ頃　雷を伴い　非常に　激しく　降る", timeSeries1_1.forecastAreas!![0].weathers!![0])
            assertEquals("くもり　昼前　晴れ　所により　夕方　から　夜のはじめ頃　雨　で　雷を伴い　非常に　激しく　降る", timeSeries1_1.forecastAreas!![0].weathers!![1])
            assertEquals("南の風　後　南東の風", timeSeries1_1.forecastAreas!![0].winds!![0])
            assertEquals("南の風　後　南東の風", timeSeries1_1.forecastAreas!![0].winds!![1])

            val timeSeries1_2 = forecast1.timeSeriesList!![1]
            assertEquals(7, timeSeries1_2.timeDefines!!.count())
            assertEquals("2023-07-12T06:00:00+09:00", timeSeries1_2.timeDefines!![0])
            assertEquals("2023-07-13T18:00:00+09:00", timeSeries1_2.timeDefines!![6])
            assertEquals(2, timeSeries1_2.forecastAreas!!.count())
            assertEquals("南部", timeSeries1_2.forecastAreas!![0].area!!.name)
            assertEquals("090010", timeSeries1_2.forecastAreas!![0].area!!.code)
            assertEquals(7, timeSeries1_2.forecastAreas!![0].pops!!.count())
            assertEquals("20", timeSeries1_2.forecastAreas!![0].pops!![0])
            assertEquals("40", timeSeries1_2.forecastAreas!![0].pops!![6])

            val timeSeries1_3 = forecast1.timeSeriesList!![2]
            assertEquals(4, timeSeries1_3.timeDefines!!.count())
            assertEquals("2023-07-12T09:00:00+09:00", timeSeries1_3.timeDefines!![0])
            assertEquals("2023-07-13T09:00:00+09:00", timeSeries1_3.timeDefines!![3])
            assertEquals(2, timeSeries1_3.forecastAreas!!.count())
            assertEquals("宇都宮", timeSeries1_3.forecastAreas!![0].area!!.name)
            assertEquals("41277", timeSeries1_3.forecastAreas!![0].area!!.code)
            assertEquals(4, timeSeries1_3.forecastAreas!![0].temps!!.count())
            assertEquals("36", timeSeries1_3.forecastAreas!![0].temps!![0])
            assertEquals("34", timeSeries1_3.forecastAreas!![0].temps!![3])

            val forecast2 = forecastList[1]
            assertEquals("宇都宮地方気象台", forecast2.publishingOffice)
            assertEquals("2023-07-11T17:00:00+09:00", forecast2.reportDatetime)
            assertEquals(2, forecast2.timeSeriesList!!.count())

            val timeSeries2_1 = forecast2.timeSeriesList!![0]
            assertEquals(7, timeSeries2_1.timeDefines!!.count())
            assertEquals("2023-07-12T00:00:00+09:00", timeSeries2_1.timeDefines!![0])
            assertEquals("2023-07-18T00:00:00+09:00", timeSeries2_1.timeDefines!![6])
            assertEquals(1, timeSeries2_1.forecastAreas!!.count())
            assertEquals("栃木県", timeSeries2_1.forecastAreas!![0].area!!.name)
            assertEquals("090000", timeSeries2_1.forecastAreas!![0].area!!.code)
            assertEquals(7, timeSeries2_1.forecastAreas!![0].weatherCodes!!.count())
            assertEquals("212", timeSeries2_1.forecastAreas!![0].weatherCodes!![0])
            assertEquals("101", timeSeries2_1.forecastAreas!![0].weatherCodes!![6])
            assertEquals(7, timeSeries2_1.forecastAreas!![0].pops!!.count())
            assertEquals("", timeSeries2_1.forecastAreas!![0].pops!![0])
            assertEquals("20", timeSeries2_1.forecastAreas!![0].pops!![6])
            assertEquals(7, timeSeries2_1.forecastAreas!![0].reliabilities!!.count())
            assertEquals("", timeSeries2_1.forecastAreas!![0].reliabilities!![0])
            assertEquals("B", timeSeries2_1.forecastAreas!![0].reliabilities!![6])

            val timeSeries2_2 = forecast2.timeSeriesList!![1]
            assertEquals(7, timeSeries2_2.timeDefines!!.count())
            assertEquals("2023-07-12T00:00:00+09:00", timeSeries2_2.timeDefines!![0])
            assertEquals("2023-07-18T00:00:00+09:00", timeSeries2_2.timeDefines!![6])
            assertEquals(1, timeSeries2_2.forecastAreas!!.count())
            assertEquals("宇都宮", timeSeries2_2.forecastAreas!![0].area!!.name)
            assertEquals("41277", timeSeries2_2.forecastAreas!![0].area!!.code)
            assertEquals(7, timeSeries2_2.forecastAreas!![0].tempsMin!!.count())
            assertEquals("", timeSeries2_2.forecastAreas!![0].tempsMin!![0])
            assertEquals("24", timeSeries2_2.forecastAreas!![0].tempsMin!![6])
            assertEquals(7, timeSeries2_2.forecastAreas!![0].tempsMinUpper!!.count())
            assertEquals("", timeSeries2_2.forecastAreas!![0].tempsMinUpper!![0])
            assertEquals("26", timeSeries2_2.forecastAreas!![0].tempsMinUpper!![6])
            assertEquals(7, timeSeries2_2.forecastAreas!![0].tempsMinLower!!.count())
            assertEquals("", timeSeries2_2.forecastAreas!![0].tempsMinLower!![0])
            assertEquals("22", timeSeries2_2.forecastAreas!![0].tempsMinLower!![6])
            assertEquals(7, timeSeries2_2.forecastAreas!![0].tempsMax!!.count())
            assertEquals("", timeSeries2_2.forecastAreas!![0].tempsMax!![0])
            assertEquals("34", timeSeries2_2.forecastAreas!![0].tempsMax!![6])
            assertEquals(7, timeSeries2_2.forecastAreas!![0].tempsMaxUpper!!.count())
            assertEquals("", timeSeries2_2.forecastAreas!![0].tempsMaxUpper!![0])
            assertEquals("37", timeSeries2_2.forecastAreas!![0].tempsMaxUpper!![6])
            assertEquals(7, timeSeries2_2.forecastAreas!![0].tempsMaxLower!!.count())
            assertEquals("", timeSeries2_2.forecastAreas!![0].tempsMaxLower!![0])
            assertEquals("31", timeSeries2_2.forecastAreas!![0].tempsMaxLower!![6])

            assertEquals(1, forecast2.tempAverage!!.averageAreas!!.count())
            assertEquals("宇都宮", forecast2.tempAverage!!.averageAreas!![0].area!!.name)
            assertEquals("41277", forecast2.tempAverage!!.averageAreas!![0].area!!.code)
            assertEquals("21.3", forecast2.tempAverage!!.averageAreas!![0].min)
            assertEquals("29.3", forecast2.tempAverage!!.averageAreas!![0].max)

            assertEquals(1, forecast2.precipAverage!!.averageAreas!!.count())
            assertEquals("宇都宮", forecast2.precipAverage!!.averageAreas!![0].area!!.name)
            assertEquals("41277", forecast2.precipAverage!!.averageAreas!![0].area!!.code)
            assertEquals("26.1", forecast2.precipAverage!!.averageAreas!![0].min)
            assertEquals("54.0", forecast2.precipAverage!!.averageAreas!![0].max)
        }
    }

}