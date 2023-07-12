package com.jozu.weatherforecast.domain.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStreamReader

/**
 *
 * Created by jozuko on 2023/07/06.
 * Copyright (c) 2023 Studio Jozu. All rights reserved.
 */
@RunWith(AndroidJUnit4::class)
class AreaTest {
    @Test
    fun readJson() {
        val androidTestContext = InstrumentationRegistry.getInstrumentation().context
        InputStreamReader(androidTestContext.assets.open("area.json")).use { inputStreamReader ->
            val area = Gson().fromJson(inputStreamReader, AreaApiModel::class.java)
            assert(area != null)

            val center = area.centers!!["010100"]
            assert(center != null)
            assertEquals("北海道地方", center!!.name)
            assertEquals("Hokkaido", center.enName)
            assertEquals(8, center.children!!.count())
            assertEquals("011000", center.children!![0])

            val office = area.offices!![center.children!![0]]
            assertEquals("宗谷地方", office!!.name)
            assertEquals("Soya", office.enName)
            assertEquals("稚内地方気象台", office.officeName)
            assertEquals("010100", office.parent)
            assertEquals(1, office.children!!.count())
            assertEquals("011000", office.children!![0])

            val class10 = area.class10s!![office.children!![0]]
            assertEquals("宗谷地方", class10!!.name)
            assertEquals("Soya Region", class10.enName)
            assertEquals("011000", class10.parent)
            assertEquals(3, class10.children!!.count())
            assertEquals("011011", class10.children!![0])

            val class15 = area.class15s!![class10.children!![0]]
            assertEquals("宗谷北部", class15!!.name)
            assertEquals("Northern Soya", class15.enName)
            assertEquals("011000", class15.parent)
            assertEquals(4, class15.children!!.count())
            assertEquals("0121400", class15.children!![0])

            val class20 = area.class20s!![class15.children!![0]]
            assertEquals("稚内市", class20!!.name)
            assertEquals("Wakkanai City", class20.enName)
            assertEquals("わっかないし", class20.kana)
            assertEquals("011011", class20.parent)
        }
    }
}