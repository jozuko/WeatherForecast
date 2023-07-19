package com.jozu.weatherforecast.domain.adapter

import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Center
import com.jozu.weatherforecast.domain.Office
import com.jozu.weatherforecast.infrastructure.api.model.AreaApiModel
import com.jozu.weatherforecast.infrastructure.db.model.CenterEntity
import com.jozu.weatherforecast.infrastructure.db.model.OfficeEntity

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
object AreaAdapter {
    fun adaptFromApi(apiModel: AreaApiModel): Area {
        val offices = apiModel.offices?.map { apiOffice ->
            Office(
                code = apiOffice.key,
                name = apiOffice.value.name ?: "",
            )
        } ?: emptyList()
        val centers = apiModel.centers?.map { apiCenter ->
            Center(
                code = apiCenter.key,
                name = apiCenter.value.name ?: "",
                offices = apiCenter.value.children?.mapNotNull { childId -> offices.firstOrNull { it.code == childId } } ?: emptyList(),
            )
        } ?: emptyList()
        return Area(centers = centers)
    }

    fun adaptFromDb(areaEntity: Map<CenterEntity, List<OfficeEntity>>): Area {
        return Area(
            centers = areaEntity.entries.map { (centerEntity, officeEntityList) ->
                Center(
                    code = centerEntity.code,
                    name = centerEntity.name,
                    offices = officeEntityList.map { officeEntity -> Office(code = officeEntity.code, name = officeEntity.name) }
                )
            },
        )
    }
}