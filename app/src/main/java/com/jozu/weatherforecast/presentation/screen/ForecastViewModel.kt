package com.jozu.weatherforecast.presentation.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozu.weatherforecast.domain.Center
import com.jozu.weatherforecast.domain.Forecast
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.Office
import com.jozu.weatherforecast.usecase.GetAreaUseCase
import com.jozu.weatherforecast.usecase.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Flowのcollect()とcollectLatest()どっち使う問題
 * https://ked4ma.medium.com/memo-flow%E3%81%AEcollect-%E3%81%A8collectlatest-%E3%81%A9%E3%81%A3%E3%81%A1%E4%BD%BF%E3%81%86%E5%95%8F%E9%A1%8C-cdb3018bc8b5
 *
 * Android でのコルーチンに関するベスト プラクティス
 * https://developer.android.com/kotlin/coroutines/coroutines-best-practices?hl=ja
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getAreaUseCase: GetAreaUseCase,
    private val getForecastUseCase: GetForecastUseCase,
) : ViewModel() {
    private val _areaFutureStateFlow: MutableStateFlow<ForecastViewModelState> = MutableStateFlow(ForecastViewModelState())
    val forecastUiStateStateFlow: StateFlow<ForecastUiState> = _areaFutureStateFlow
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = _areaFutureStateFlow.value.toUiState(),
        )

    private var _forecastFutureState: MutableStateFlow<Future<Forecast>> = MutableStateFlow(Future.Idle)
    val forecastFutureState: StateFlow<Future<Forecast>> = _forecastFutureState.asStateFlow()

    private var _isShowCenterSelectDialog: MutableState<Boolean> = mutableStateOf(false)
    val isShowCenterSelectDialog: State<Boolean> = _isShowCenterSelectDialog

    private var _isShowOfficeSelectDialog: MutableState<Boolean> = mutableStateOf(false)
    val isShowOfficeSelectDialog: State<Boolean> = _isShowOfficeSelectDialog

    init {
        refreshArea()
    }

    fun refreshArea() {
        _areaFutureStateFlow.value = ForecastViewModelState(areaFuture = Future.Proceeding)

        viewModelScope.launch {
            getAreaUseCase().collect {
                _areaFutureStateFlow.value = ForecastViewModelState(areaFuture = it)
            }
        }
    }

    fun selectAreaCenter(selected: Center) {
        val uiState = forecastUiStateStateFlow.value
        if (uiState.center != selected) {
            _areaFutureStateFlow.value = ForecastViewModelState(
                areaFuture = uiState.areaFuture,
                center = selected,
                office = selected.offices.firstOrNull(),
            )
        }
    }

    fun selectAreaOffice(selected: Office) {
        val uiState = forecastUiStateStateFlow.value
        if (uiState.office != selected) {
            _areaFutureStateFlow.value = ForecastViewModelState(
                areaFuture = uiState.areaFuture,
                center = uiState.center,
                office = selected,
            )
        }
    }

    fun showCenterSelectDialog() {
        _isShowCenterSelectDialog.value = true
    }

    fun hideCenterSelectDialog() {
        _isShowCenterSelectDialog.value = false
    }

    fun showOfficeSelectDialog() {
        _isShowOfficeSelectDialog.value = true
    }

    fun hideOfficeSelectDialog() {
        _isShowOfficeSelectDialog.value = false
    }

    fun searchForecast() {
        viewModelScope.launch {
            suspendSearchForecast()
        }
    }

    private suspend fun suspendSearchForecast() {
        _forecastFutureState.emit(Future.Idle)
        val office = _areaFutureStateFlow.value.office ?: return

        _forecastFutureState.emit(Future.Proceeding)

        getForecastUseCase(office).collect {
            _forecastFutureState.emit(it)
            if (it is Future.Success) {
                Log.d("Search!!", "${it.value}")
            }
        }
    }
}