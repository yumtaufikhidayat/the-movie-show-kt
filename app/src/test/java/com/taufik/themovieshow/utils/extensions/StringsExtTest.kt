package com.taufik.themovieshow.utils.extensions

import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants.FULL_FORMATS
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants.YYYY_FORMAT
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants.YYYY_MM_DD_FORMAT
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class StringsExtTest {

    @Test
    fun `review timestamp with milliseconds is formatted`() {
        assertEquals(
            "Wed, 23 Jun 2021",
            "2021-06-23T15:58:28.123Z".convertDate(FULL_FORMATS, EEE_D_MMM_YYYY_FORMAT)
        )
    }

    @Test
    fun `review timestamp without milliseconds is formatted`() {
        assertEquals(
            "Sat, 10 Oct 2026",
            "2026-10-10T16:50:38Z".convertDate(FULL_FORMATS, EEE_D_MMM_YYYY_FORMAT)
        )
    }

    @Test
    fun `release date is formatted`() {
        assertEquals("2026", "2026-10-09".convertDate(YYYY_MM_DD_FORMAT, YYYY_FORMAT))
    }

    @Test
    fun `blank input returns null instead of today's date`() {
        assertNull("".convertDate(YYYY_MM_DD_FORMAT, YYYY_FORMAT))
        assertNull("  ".convertDate(YYYY_MM_DD_FORMAT, YYYY_FORMAT))
    }

    @Test
    fun `unparseable input returns null instead of throwing`() {
        assertNull("not a date".convertDate(FULL_FORMATS, EEE_D_MMM_YYYY_FORMAT))
        assertNull("2026-10-10T16:50:38Z".convertDate(YYYY_FORMAT + "/MM", EEE_D_MMM_YYYY_FORMAT))
    }
}
