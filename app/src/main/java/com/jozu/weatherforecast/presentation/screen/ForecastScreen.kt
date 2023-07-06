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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.repository.Area

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
        val areaState = viewModel.areaFuture
        val modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()

        when (areaState) {
            is Future.Proceeding -> LoadingScreen(modifier)
            is Future.Error -> ErrorScreen(modifier, viewModel)
            is Future.Success -> DataScreen(modifier, viewModel, areaState.value)
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 10.dp
        )
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier,
    viewModel: ForecastViewModel
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text("読み込み失敗")

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { viewModel.refreshArea() }) {
                Text("再読み込み")
            }
        }
    }
}

@Composable
fun DataScreen(
    modifier: Modifier,
    viewModel: ForecastViewModel,
    area: Area,
) {
    val centerIndex = viewModel.centerIndex
    val centerName = area.centers.entries.toList()[centerIndex].value.name

    Box(modifier = modifier.padding(8.dp)) {
        Column {
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(centerName)
                }
            }

            Button(onClick = { /*TODO*/ }) {
                Text("検索")
            }
        }
    }

}