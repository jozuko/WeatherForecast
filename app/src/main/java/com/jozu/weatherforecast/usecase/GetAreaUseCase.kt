package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.domain.Future
import com.jozu.weatherforecast.domain.repository.Area
import kotlinx.coroutines.flow.Flow

/**
 * 予報地点情報の取得ユースケース
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
interface GetAreaUseCase {
    suspend fun invoke(): Flow<Future<Area>>
}