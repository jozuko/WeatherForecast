package com.jozu.weatherforecast.infrastructure.repository

import android.util.Log
import androidx.room.withTransaction
import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.AreaRepository
import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.adapter.AreaAdapter
import com.jozu.weatherforecast.domain.extension.LongEx.isPassedTime
import com.jozu.weatherforecast.infrastructure.api.ForecastApi
import com.jozu.weatherforecast.infrastructure.api.apiFlow
import com.jozu.weatherforecast.infrastructure.db.AppDatabase
import com.jozu.weatherforecast.infrastructure.db.AreaDao
import com.jozu.weatherforecast.infrastructure.db.model.CenterEntity
import com.jozu.weatherforecast.infrastructure.db.model.OfficeEntity
import com.jozu.weatherforecast.infrastructure.pref.SharedPref
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
class AreaRepositoryImpl @Inject constructor(
    private val forecastApi: ForecastApi,
    private val db: AppDatabase,
    private val areaDao: AreaDao,
    private val sharedPref: SharedPref,
    private val dispatchers: CoroutineDispatcher,
) : AreaRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getArea(): Flow<Future<Area>> {
        return flow {
            val areaDataUpdatedAt = sharedPref.areaDataUpdatedAt
            emit(areaDataUpdatedAt.isPassedTime(minutes = 10))
        }.flatMapConcat { needServerData ->
            if (needServerData) {
                getAreaFromServer()
            } else {
                getAreaFromLocal()
            }
        }.onStart {
            emit(Future.Proceeding)
        }.flowOn(dispatchers)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAreaFromServer(): Flow<Future<Area>> {
        return apiFlow(dispatchers) {
            Log.d("AreaRepository", "getAreaFromServer")
            forecastApi.getArea()
        }.map { apiModelFuture ->
            when (apiModelFuture) {
                is Future.Error -> Future.Error(apiModelFuture.error)
                is Future.Success -> Future.Success(AreaAdapter.adaptFromApi(apiModelFuture.value))
                is Future.Proceeding -> Future.Idle
                is Future.Idle -> Future.Idle
            }
        }.flatMapConcat { areaFuture ->
            if (areaFuture is Future.Success) {
                saveArea(areaFuture.value)
            } else {
                flow { emit(areaFuture) }
            }
        }.flowOn(dispatchers)
    }

    private fun getAreaFromLocal(): Flow<Future<Area>> {
        return flow<Future<Area>> {
            Log.d("AreaRepository", "getAreaFromLocal")

            val areaMap = areaDao.getAll()
            val area = AreaAdapter.adaptFromDb(areaMap)
            emit(Future.Success(area))
        }.catch { cause ->
            emit(Future.Error(cause))
        }.flowOn(dispatchers)
    }

    private fun saveArea(area: Area): Flow<Future<Area>> {
        return flow<Future<Area>> {
            Log.d("AreaRepository", "saveArea")

            db.withTransaction {
                areaDao.deleteAllCenter()
                areaDao.deleteAllOffice()

                area.centers.forEach { center ->
                    val centerEntity = CenterEntity(code = center.code, name = center.name)
                    val officeEntityList = center.offices.map { office ->
                        OfficeEntity(code = office.code, name = office.name, centerCode = center.code)
                    }

                    areaDao.insertCenter(centerEntity)
                    areaDao.insertAllOffice(officeEntityList)
                }
            }

            sharedPref.areaDataUpdatedAt = System.currentTimeMillis()
            emit(Future.Success(area))
        }.catch { cause ->
            emit(Future.Error(cause))
        }.flowOn(dispatchers)
    }
}