package com.jozu.weatherforecast.presentation.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.repository.Area
import com.jozu.weatherforecast.usecase.GetAreaUseCase
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
) : ViewModel() {
    var areaFuture: Future<Area> by mutableStateOf(Future.Proceeding)
    var centerIndex: Int by mutableStateOf(1)

    init {
        refreshArea()
    }

    fun refreshArea() {
        viewModelScope.launch {
            areaFuture = Future.Proceeding
            getAreaUseCase.invoke().collectLatest {
                areaFuture = it
                if (it is Future.Success) {
                    centerIndex = 0
                }
            }
        }
    }

    fun selectAreaCenter(index: Int) {
        viewModelScope.launch {
            centerIndex = index
        }
    }
}