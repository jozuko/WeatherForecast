package com.jozu.weatherforecast.domain.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 *
 * Created by jozuko on 2023/07/12.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
object CalendarEx {
    fun parse(format: String, value: String): Calendar? {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()

        return try {
            dateFormat.parse(value)?.let { date ->
                Calendar.getInstance().apply {
                    timeInMillis = date.time
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}