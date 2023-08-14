package com.jozu.weatherforecast.infrastructure.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Singleton
class SharedPref @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "forecast_pref_datastore")

    private val keyAreaDataUpdatedAt = longPreferencesKey(SharedPrefKey.AreaDataUpdatedAt.name)

    val areaDataUpdatedAt: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[keyAreaDataUpdatedAt] ?: 0L
    }

    suspend fun editAreaDataUpdatedAt(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[keyAreaDataUpdatedAt] = value
        }
    }
}