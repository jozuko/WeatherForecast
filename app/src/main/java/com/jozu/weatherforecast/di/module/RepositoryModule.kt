package com.jozu.weatherforecast.di.module

import com.jozu.weatherforecast.domain.AreaRepository
import com.jozu.weatherforecast.domain.ForecastRepository
import com.jozu.weatherforecast.infrastructure.repository.AreaRepositoryImpl
import com.jozu.weatherforecast.infrastructure.repository.ForecastRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideRepositoryDispatcher(
    ): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideAreaRepository(
        impl: AreaRepositoryImpl
    ): AreaRepository = impl

    @Singleton
    @Provides
    fun provideForecastRepository(
        impl: ForecastRepositoryImpl
    ): ForecastRepository = impl
}