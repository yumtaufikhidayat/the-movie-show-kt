package com.taufik.themovieshow.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class IntExtTest {

    @Test
    fun `toThousandFormat formats numbers with comma separators`() {
        assertEquals("0", 0.toThousandFormat())
        assertEquals("999", 999.toThousandFormat())
        assertEquals("1,000", 1000.toThousandFormat())
        assertEquals("12,345", 12345.toThousandFormat())
        assertEquals("1,000,000", 1000000.toThousandFormat())
        assertEquals("123,456,789", 123456789.toThousandFormat())
    }

    @Test
    fun `toThousandFormat handles negative numbers`() {
        assertEquals("-1,000", (-1000).toThousandFormat())
        assertEquals("-12,345", (-12345).toThousandFormat())
    }
}
