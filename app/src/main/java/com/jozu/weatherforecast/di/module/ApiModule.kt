package com.jozu.weatherforecast.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jozu.weatherforecast.infrastructure.repository.ForecastRepository
import com.jozu.weatherforecast.infrastructure.repository.api.ForecastApi
import com.jozu.weatherforecast.usecase.GetAreaUseCase
import com.jozu.weatherforecast.usecase.GetAreaUseCaseImpl
import com.jozu.weatherforecast.usecase.GetForecastUseCase
import com.jozu.weatherforecast.usecase.GetForecastUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideForecastApi(
        retrofit: Retrofit,
    ): ForecastApi {
        return retrofit.create(ForecastApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.jma.go.jp/bosai/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideGetAreaUseCase(
        forecastRepository: ForecastRepository,
    ): GetAreaUseCase = GetAreaUseCaseImpl(forecastRepository)

    @Singleton
    @Provides
    fun provideGetForecastUseCase(
        forecastRepository: ForecastRepository,
    ): GetForecastUseCase = GetForecastUseCaseImpl(forecastRepository)
}