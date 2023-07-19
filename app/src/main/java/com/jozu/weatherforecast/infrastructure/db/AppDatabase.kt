package com.jozu.weatherforecast.infrastructure.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jozu.weatherforecast.infrastructure.db.model.CenterEntity
import com.jozu.weatherforecast.infrastructure.db.model.OfficeEntity

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Database(entities = [CenterEntity::class, OfficeEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun areaDao(): AreaDao
}