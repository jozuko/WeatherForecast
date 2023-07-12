package com.jozu.weatherforecast.presentation.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Center
import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.Office
import com.jozu.weatherforecast.usecase.GetAreaUseCase
import com.jozu.weatherforecast.usecase.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getAreaUseCase: GetAreaUseCase,
    private val getForecastUseCase: GetForecastUseCase,
) : ViewModel() {
    var areaFuture: Future<Area> by mutableStateOf(Future.Idel)
        private set

    var forecastFuture: Future<Forecast> by mutableStateOf(Future.Idel)
        private set

    var center: Center? by mutableStateOf(null)
        private set

    var office: Office? by mutableStateOf(null)
        private set

    var isShowCenterSelectDialog: Boolean by mutableStateOf(false)
        private set

    var isShowOfficeSelectDialog: Boolean by mutableStateOf(false)
        private set

    init {
        refreshArea()
    }

    fun refreshArea() {
        viewModelScope.launch {
            areaFuture = Future.Proceeding
            getAreaUseCase.invoke().collectLatest {
                areaFuture = it
                if (it is Future.Success) {
                    center = it.value.centers.firstOrNull()
                    office = center?.offices?.firstOrNull()
                }
            }
        }
    }

    fun selectAreaCenter(selected: Center) {
        if (center != selected) {
            viewModelScope.launch {
                val areaData: Future<Area> = areaFuture
                if (areaData is Future.Success) {
                    center = selected
                    office = center?.offices?.firstOrNull()
                }
            }
        }
    }

    fun selectAreaOffice(selected: Office) {
        if (office != selected) {
            viewModelScope.launch {
                office = selected
            }
        }
    }

    fun showCenterSelectDialog() {
        viewModelScope.launch {
            isShowCenterSelectDialog = true
        }
    }

    fun hideCenterSelectDialog() {
        viewModelScope.launch {
            isShowCenterSelectDialog = false
        }
    }

    fun showOfficeSelectDialog() {
        viewModelScope.launch {
            isShowOfficeSelectDialog = true
        }
    }

    fun hideOfficeSelectDialog() {
        viewModelScope.launch {
            isShowOfficeSelectDialog = false
        }
    }

    fun searchForecast() {
        val office = office ?: return

        viewModelScope.launch {
            forecastFuture = Future.Proceeding
            getForecastUseCase.invoke(office).collectLatest {
                forecastFuture = it
                if (it is Future.Success) {
                    Log.d("Search!!", "${it.value}")
                }
            }
        }
    }
}