package com.jozu.weatherforecast.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.presentation.dialog.SingleSelectionDialog
import com.jozu.weatherforecast.presentation.dialog.SingleSelectionSlideInDialog

/**
 * 天気予報を検索・表示する画面
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        val uiState = viewModel.forecastUiStateStateFlow.collectAsState().value
        val modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()

        when (uiState.areaFuture) {
            is Future.Idle -> LoadingScreen(modifier)
            is Future.Proceeding -> LoadingScreen(modifier)
            is Future.Error -> ErrorScreen(modifier, viewModel)
            is Future.Success -> {
                DataScreen(modifier, viewModel, uiState)
                CenterSelectDialog(viewModel, uiState, uiState.areaFuture.value)
                OfficeSelectDialog(viewModel, uiState)
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
    uiState: ForecastUiState,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Button(
                    onClick = {
                        viewModel.showCenterSelectDialog()
                    },
                ) {
                    Text(uiState.center?.name ?: "未選択")
                }
                Button(
                    onClick = {
                        viewModel.showOfficeSelectDialog()
                    },
                ) {
                    Text(uiState.office?.name ?: "未選択")
                }
            }

            Box(
                modifier = Modifier.align(Alignment.End),
            ) {
                Button(onClick = {
                    viewModel.searchForecast()
                }) {
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
                    Text(forecastState.value.toString())
                }
            }
        }
    }
}

@Composable
private fun CenterSelectDialog(
    viewModel: ForecastViewModel,
    uiState: ForecastUiState,
    area: Area,
) {
    if (viewModel.isShowCenterSelectDialog.value) {
        val centers = area.centers

        SingleSelectionSlideInDialog(
            title = "地方選択",
            labels = centers.map { it.name },
            selection = centers,
            defaultSelectedIndex = centers.indexOfFirst { it == uiState.center },
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
    uiState: ForecastUiState,
) {
    if (viewModel.isShowOfficeSelectDialog.value) {
        val offices = uiState.center?.offices ?: return

        SingleSelectionDialog(
            title = "事務所選択",
            labels = offices.map { it.name },
            selection = offices,
            defaultSelectedIndex = offices.indexOfFirst { it == uiState.office },
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
