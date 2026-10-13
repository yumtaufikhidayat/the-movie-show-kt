package com.taufik.themovieshow.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class DoubleExtTest {

    @Test
    fun `toRating rounds to one decimal place correctly`() {
        assertEquals("7.8", 7.84.toRating())
        assertEquals("7.9", 7.86.toRating())
        assertEquals("7.9", 7.85.toRating())
        assertEquals("8.0", 8.0.toRating())
        assertEquals("0.0", 0.0.toRating())
        assertEquals("10.0", 10.0.toRating())
    }

    @Test
    fun `toRating handles negative values`() {
        assertEquals("-5.5", (-5.49).toRating())
        assertEquals("-1.0", (-1.0).toRating())
    }
}
