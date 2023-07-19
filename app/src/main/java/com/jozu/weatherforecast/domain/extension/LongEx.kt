package com.jozu.weatherforecast.domain.extension

/**
 *
 * Created by jozuko on 2023/07/19.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
object LongEx {
    fun Long.isPassedTime(minutes: Int): Boolean {
        if (this == 0L) {
            return true
        }
        val current = System.currentTimeMillis()
        return ((this + minutes * 60 * 1000L) <= current)
    }
}