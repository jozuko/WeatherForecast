package com.jozu.weatherforecast.infrastructure.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jozu.weatherforecast.infrastructure.db.model.CenterEntity
import com.jozu.weatherforecast.infrastructure.db.model.OfficeEntity

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Dao
interface AreaDao {
    @Query("select * from center join office on center.code = office.center_code order by center.code, office.code")
    fun getAll(): Map<CenterEntity, List<OfficeEntity>>

    @Query("delete from center")
    fun deleteAllCenter()

    @Query("delete from office")
    fun deleteAllOffice()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCenter(centerEntity: CenterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllOffice(officeEntityList: List<OfficeEntity>)
}