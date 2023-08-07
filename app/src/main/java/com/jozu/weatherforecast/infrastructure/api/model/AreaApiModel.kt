package com.jozu.weatherforecast.infrastructure.api.model

import kotlinx.serialization.Serializable

/**
 * 天気予報地点情報
 * https://www.jma.go.jp/jma/kishou/info/coment.html
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@Serializable
data class AreaApiModel(
    val centers: Map<String, CenterApiModel>?,
    val offices: Map<String, OfficeApiModel>?,
    val class10s: Map<String, Class10ApiModel>?,
    val class15s: Map<String, Class15ApiModel>?,
    val class20s: Map<String, Class20ApiModel>?,
)

@Serializable
data class CenterApiModel(
    val name: String?,
    val enName: String?,
    val officeName: String?,
    val children: List<String>?,
)

@Serializable
data class OfficeApiModel(
    val name: String?,
    val enName: String?,
    val officeName: String?,
    val parent: String?,
    val children: List<String>?,
)

@Serializable
data class Class10ApiModel(
    val name: String?,
    val enName: String?,
    val parent: String?,
    val children: List<String>?,
)

@Serializable
data class Class15ApiModel(
    val name: String?,
    val enName: String?,
    val parent: String?,
    val children: List<String>?,
)

@Serializable
data class Class20ApiModel(
    val name: String?,
    val enName: String?,
    val kana: String?,
    val parent: String?,
)
