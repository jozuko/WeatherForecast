package com.jozu.weatherforecast.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jozu.weatherforecast.domain.ForecastOverview
import com.jozu.weatherforecast.domain.WeatherCode
import com.jozu.weatherforecast.domain.extension.CalendarEx.format
import java.util.Calendar

/**
 *
 * Created by jozuko on 2023/07/17.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Composable
fun ForecastOverviewBox(forecastOverview: ForecastOverview) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(size = 16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(all = 16.dp),
        ) {
            DayText(
                modifier = Modifier.align(alignment = Alignment.Start),
                day = forecastOverview.time,
            )

            WeatherCodeImage(
                modifier = Modifier
                    .size(200.dp, 150.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                weatherCode = forecastOverview.weatherCode,
            )

            WeatherDesc(weather = forecastOverview.weather)

            WindDesc(wind = forecastOverview.wind)
        }
    }
}

/**
 * 年月日（年、月、日は小さいフォントにする）
 */
@Composable
private fun DayText(modifier: Modifier = Modifier, day: Calendar) {
    val smallSpanStyle = SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
    Text(
        text = buildAnnotatedString {
            append(day.format("yy"))
            withStyle(style = smallSpanStyle) {
                append("年")
            }
            append(day.format("M"))
            withStyle(style = smallSpanStyle) {
                append("月")
            }
            append(day.format("d"))
            withStyle(style = smallSpanStyle) {
                append("日")
            }
        },
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier,
    )
}

@Composable
private fun WeatherCodeImage(modifier: Modifier = Modifier, weatherCode: WeatherCode) {
    val weatherCodeUrl = "https://www.jma.go.jp/bosai/forecast/img/${weatherCode.image}"

    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current).data(weatherCodeUrl).decoderFactory(SvgDecoder.Factory()).build(),
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun WeatherDesc(modifier: Modifier = Modifier, weather: String) {
    AutoSizeText(
        text = weather,
        autoSizeMaxTextSize = MaterialTheme.typography.bodyMedium.fontSize.value,
        autoSizeMinTextSize = MaterialTheme.typography.bodySmall.fontSize.value,
        maxLines = 1,
        modifier = modifier,
    )
}

@Composable
private fun WindDesc(modifier: Modifier = Modifier, wind: String) {
    AutoSizeText(
        text = wind,
        autoSizeMaxTextSize = MaterialTheme.typography.bodyMedium.fontSize.value,
        autoSizeMinTextSize = MaterialTheme.typography.bodySmall.fontSize.value,
        maxLines = 1,
        modifier = modifier,
    )
}