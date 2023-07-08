package com.jozu.weatherforecast.domain.repository

/**
 * 天気予報地点情報
 * https://www.jma.go.jp/jma/kishou/info/coment.html
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
data class Area(
    val centers: Map<String, Center>,
    val offices: Map<String, Office>,
    val class10s: Map<String, Class10>,
    val class15s: Map<String, Class15>,
    val class20s: Map<String, Class20>,
) {
    fun getCenter(index: Int): Pair<String, Center>? {
        val centerList = centers.entries.toList()
        if (centerList.isEmpty()) {
            return null
        }

        if (index < 0) {
            val firstCenter = centerList.first()
            return Pair(firstCenter.key, firstCenter.value)
        }

        if (index >= centerList.count()) {
            val firstCenter = centerList.first()
            return Pair(firstCenter.key, firstCenter.value)
        }

        val center = centerList[index]
        return Pair(center.key, center.value)
    }

    fun getCenterOffices(centerId: String): List<Pair<String, Office>> {
        val center = centers[centerId] ?: return listOf()

        return offices.mapNotNull { (officeId, office) ->
            if (center.children.contains(officeId)) {
                Pair(officeId, office)
            } else {
                null
            }
        }
    }

    fun getOffice(centerId: String, officeIndex: Int): Pair<String, Office>? {
        val center = centers[centerId] ?: return null

        val officeList = offices.mapNotNull { (officeId, office) ->
            if (center.children.contains(officeId)) {
                Pair(officeId, office)
            } else {
                null
            }
        }

        if (officeList.isEmpty()) {
            return null
        }
        if (officeIndex < 0) {
            return officeList.first()
        }
        if (officeIndex >= officeList.count()) {
            return officeList.first()
        }

        return officeList[officeIndex]
    }
}

data class Center(
    val name: String,
    val enName: String,
    val officeName: String,
    val children: List<String>,
)

data class Office(
    val name: String,
    val enName: String,
    val officeName: String,
    val parent: String,
    val children: List<String>,
)

data class Class10(
    val name: String,
    val enName: String,
    val parent: String,
    val children: List<String>,
)

data class Class15(
    val name: String,
    val enName: String,
    val parent: String,
    val children: List<String>,
)

data class Class20(
    val name: String,
    val enName: String,
    val kana: String,
    val parent: String,
)
