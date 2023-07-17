package com.jozu.weatherforecast.presentation.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.presentation.dialog.SingleSelectionDialog
import com.jozu.weatherforecast.presentation.dialog.SingleSelectionSlideInDialog

/**
 * 天気予報を検索・表示する画面
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        val areaState = viewModel.areaFutureStateFlow.collectAsState().value
        val modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()

        when (areaState) {
            is Future.Idle -> LoadingScreen(modifier)
            is Future.Proceeding -> LoadingScreen(modifier)
            is Future.Error -> ErrorScreen(modifier, viewModel)
            is Future.Success -> {
                DataScreen(modifier, viewModel)
                CenterSelectDialog(viewModel, areaState.value)
                OfficeSelectDialog(viewModel)
            }
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 10.dp,
        )
    }
}

@Composable
private fun ErrorScreen(
    modifier: Modifier,
    viewModel: ForecastViewModel,
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Column {
            Text("読み込み失敗")

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                viewModel.refreshArea()
            }) {
                Text("再読み込み")
            }
        }
    }
}

@Composable
private fun DataScreen(
    modifier: Modifier,
    viewModel: ForecastViewModel,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(),
                    shape = RoundedCornerShape(percent = 10),
                    onClick = {
                        viewModel.showCenterSelectDialog()
                    },
                ) {
                    Text(viewModel.center.value?.name ?: "未選択")
                }
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(),
                    shape = RoundedCornerShape(percent = 10),
                    onClick = {
                        viewModel.showOfficeSelectDialog()
                    },
                ) {
                    Text(viewModel.office.value?.name ?: "未選択")
                }
            }

            Box(
                modifier = Modifier.align(Alignment.End),
            ) {
                Button(onClick = {
                    viewModel.searchForecast()
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("検索")
                }
            }

            when (val forecastState = viewModel.forecastFutureState.collectAsState().value) {
                is Future.Error -> {
                    forecastState.error.localizedMessage?.let { Text(it) } ?: Text("天気予報の取得に失敗")
                }

                is Future.Idle -> {
                    Text("検索を押してください")
                }

                is Future.Proceeding -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 10.dp,
                    )
                }

                is Future.Success -> {
                    ForecastPager(forecastState.value)
                }
            }
        }
    }
}

@Composable
private fun CenterSelectDialog(
    viewModel: ForecastViewModel,
    area: Area,
) {
    if (viewModel.isShowCenterSelectDialog.value) {
        val centers = area.centers

        SingleSelectionSlideInDialog(
            title = "地方選択",
            labels = centers.map { it.name },
            selection = centers,
            defaultSelectedIndex = centers.indexOfFirst { it == viewModel.center.value },
            onConfirmRequest = { center ->
                viewModel.selectAreaCenter(center)
            },
            onDismissRequest = {
                viewModel.hideCenterSelectDialog()
            },
        )
    }
}

@Composable
private fun OfficeSelectDialog(
    viewModel: ForecastViewModel,
) {
    if (viewModel.isShowOfficeSelectDialog.value) {
        val offices = viewModel.center.value?.offices ?: return

        SingleSelectionDialog(
            title = "事務所選択",
            labels = offices.map { it.name },
            selection = offices,
            defaultSelectedIndex = offices.indexOfFirst { it == viewModel.office.value },
            onConfirmRequest = { office ->
                viewModel.selectAreaOffice(office)
                viewModel.hideOfficeSelectDialog()
            },
            onDismissRequest = {
                viewModel.hideOfficeSelectDialog()
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForecastPager(forecast: Forecast) {
    val pageState = rememberPagerState()

    HorizontalPager(
        pageCount = forecast.areaOverviews.count(),
        state = pageState,
    ) { page ->
        val areaOverview = forecast.areaOverviews[page]
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Text(
                    text = areaOverview.areaName,
                )
                val weatherCodeUrl = "https://www.jma.go.jp/bosai/forecast/img/${areaOverview.overviews[page].weatherCode.image}"
                Log.d("weatherCodeUrl", weatherCodeUrl)
                AsyncImage(
                    modifier = Modifier.size(200.dp, 200.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(weatherCodeUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }

}