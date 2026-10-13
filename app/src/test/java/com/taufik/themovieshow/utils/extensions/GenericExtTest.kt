package com.taufik.themovieshow.utils.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GenericExtTest {

    private data class SampleItem(val id: Int, val date: String?)

    private val inputFormat = "yyyy-MM-dd"
    private val thresholdFormat = "yyyy-MM-dd"
    private val threshold = "2020-01-01"

    @Test
    fun `filterAndSortByDate filters items older than threshold and sorts descending`() {
        val items = listOf(
            SampleItem(1, "2019-12-31"), // Before threshold
            SampleItem(2, "2021-05-10"),
            SampleItem(3, "2020-01-01"), // Exactly on threshold
            SampleItem(4, "2023-08-20")
        )

        val result = items.filterAndSortByDate(
            getDate = { it.date },
            dateThreshold = threshold,
            inputFormat = inputFormat,
            thresholdFormat = thresholdFormat
        )

        assertEquals(listOf(4, 2, 3), result.map { it.id })
    }

    @Test
    fun `filterAndSortByDate skips null or unparseable dates`() {
        val items = listOf(
            SampleItem(1, null),
            SampleItem(2, "invalid-date"),
            SampleItem(3, "2022-03-15")
        )

        val result = items.filterAndSortByDate(
            getDate = { it.date },
            dateThreshold = threshold,
            inputFormat = inputFormat,
            thresholdFormat = thresholdFormat
        )

        assertEquals(listOf(3), result.map { it.id })
    }

    @Test
    fun `filterAndSortByDate returns empty list when all items are before threshold`() {
        val items = listOf(
            SampleItem(1, "2015-01-01"),
            SampleItem(2, "2018-06-30")
        )

        val result = items.filterAndSortByDate(
            getDate = { it.date },
            dateThreshold = threshold,
            inputFormat = inputFormat,
            thresholdFormat = thresholdFormat
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `filterAndSortByDate returns empty list when dateThreshold is invalid`() {
        val items = listOf(
            SampleItem(1, "2022-01-01")
        )

        val result = items.filterAndSortByDate(
            getDate = { it.date },
            dateThreshold = "bad-threshold",
            inputFormat = inputFormat,
            thresholdFormat = thresholdFormat
        )

        assertTrue(result.isEmpty())
    }
}
