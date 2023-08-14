package com.jozu.weatherforecast.infrastructure.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class SharedPref @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val keyAreaDataUpdatedAt = longPreferencesKey(SharedPrefKey.AreaDataUpdatedAt.name)

    val areaDataUpdatedAt: Flow<Long> = dataStore.data.map { preferences ->
        preferences[keyAreaDataUpdatedAt] ?: 0L
    }

    suspend fun editAreaDataUpdatedAt(value: Long) {
        dataStore.edit { preferences ->
            preferences[keyAreaDataUpdatedAt] = value
        }
    }
}