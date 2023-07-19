package com.jozu.weatherforecast.domain.adapter

import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Center
import com.jozu.weatherforecast.domain.Office
import com.jozu.weatherforecast.infrastructure.api.model.AreaApiModel

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
object AreaAdapter {
    fun adaptFromApi(apiModel: AreaApiModel): Area {
        val offices = apiModel.offices?.map { apiOffice ->
            Office(
                id = apiOffice.key,
                name = apiOffice.value.name ?: "",
            )
        } ?: emptyList()
        val centers = apiModel.centers?.map { apiCenter ->
            Center(
                id = apiCenter.key,
                name = apiCenter.value.name ?: "",
                offices = apiCenter.value.children?.mapNotNull { childId -> offices.firstOrNull { it.id == childId } } ?: emptyList(),
            )
        } ?: emptyList()
        return Area(centers = centers)
    }
}