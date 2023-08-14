package com.jozu.weatherforecast.di.module

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jozu.weatherforecast.infrastructure.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.db.AppDatabase
import com.jozu.weatherforecast.infrastructure.db.AreaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object InfrastructureModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideKotlinSerializer(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor { message ->
                    Log.d("Forecast-API", message)
                }.apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                },
            ).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        kotlinSerializer: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit
            .Builder()
            .baseUrl("https://www.jma.go.jp/bosai/")
            .client(okHttpClient)
            .addConverterFactory(kotlinSerializer.asConverterFactory(contentType))
            .build()
    }

    @Singleton
    @Provides
    fun provideForecastApi(
        retrofit: Retrofit,
    ): ForecastApi {
        return retrofit.create(ForecastApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "forecast_db",
        ).build()
    }

    @Singleton
    @Provides
    fun provideAreaDao(
        db: AppDatabase,
    ): AreaDao {
        return db.areaDao()
    }

    private val dummyProp: Nothing? = null

    @Singleton
    @Provides
    fun providePrefDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = preferencesDataStore(name = "forecast_pref_datasource").getValue(context, InfrastructureModule::dummyProp)
}