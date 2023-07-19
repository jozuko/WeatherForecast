package com.jozu.weatherforecast.di.module

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jozu.weatherforecast.infrastructure.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.db.AppDatabase
import com.jozu.weatherforecast.infrastructure.db.AreaDao
import com.jozu.weatherforecast.infrastructure.pref.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
object InfrastructureModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).addInterceptor(
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
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.jma.go.jp/bosai/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).build()
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

    @Singleton
    @Provides
    fun provideSharePref(
        @ApplicationContext context: Context,
    ): SharedPref {
        val sharedPreferences = context.getSharedPreferences("forecast_pref", Context.MODE_PRIVATE)
        return SharedPref(sharedPreferences)
    }
}