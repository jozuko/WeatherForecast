package com.jozu.weatherforecast.infrastructure.pref

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class SharedPref(
    private val sharedPreferences: SharedPreferences,
) {
    var areaDataUpdatedAt: Long
        get() = sharedPreferences.getLong(SharedPrefKey.AreaDataUpdatedAt.name, 0L)
        set(value) = sharedPreferences.edit {
            putLong(SharedPrefKey.AreaDataUpdatedAt.name, value)
        }
}