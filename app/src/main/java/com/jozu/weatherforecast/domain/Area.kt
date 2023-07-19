package com.jozu.weatherforecast.domain

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
data class Area(
    val centers: List<Center>,
)

data class Center(
    val code: String,
    val name: String,
    val offices: List<Office>,
)

data class Office(
    val code: String,
    val name: String,
)