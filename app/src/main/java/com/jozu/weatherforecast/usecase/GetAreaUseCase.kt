package com.jozu.weatherforecast.usecase

import com.jozu.weatherforecast.domain.Area
import com.jozu.weatherforecast.domain.Future
import kotlinx.coroutines.flow.Flow

/**
 * 予報地点情報の取得ユースケース
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
interface GetAreaUseCase {
    operator fun invoke(): Flow<Future<Area>>
}