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
        private set

    var centerId: String by mutableStateOf("")
        private set

    var officeId: String by mutableStateOf("")
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
                    centerId = it.value.getCenter(0)?.first ?: ""
                    officeId = it.value.getOffice(centerId, 0)?.first ?: ""
                }
            }
        }
    }

    fun selectAreaCenter(id: String) {
        if (centerId != id) {
            viewModelScope.launch {
                val areaData: Future<Area> = areaFuture
                if (areaData is Future.Success) {
                    centerId = id
                    officeId = areaData.value.getOffice(centerId, 0)?.first ?: ""
                }
            }
        }
    }

    fun selectAreaOffice(id: String) {
        if (officeId != id) {
            viewModelScope.launch {
                officeId = id
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
}