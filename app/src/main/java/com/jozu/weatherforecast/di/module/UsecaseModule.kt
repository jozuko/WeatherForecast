package com.jozu.weatherforecast.di.module

import com.jozu.weatherforecast.domain.AreaRepository
import com.jozu.weatherforecast.domain.ForecastRepository
import com.jozu.weatherforecast.usecase.GetAreaUseCase
import com.jozu.weatherforecast.usecase.GetForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object UsecaseModule {
    @Singleton
    @Provides
    fun provideGetAreaUseCase(
        areaRepository: AreaRepository,
    ): GetAreaUseCase = GetAreaUseCase(areaRepository)

    @Singleton
    @Provides
    fun provideGetForecastUseCase(
        forecastRepository: ForecastRepository,
    ): GetForecastUseCase = GetForecastUseCase(forecastRepository)

}