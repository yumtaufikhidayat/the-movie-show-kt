package com.taufik.themovieshow.ui.about.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.about.AboutSection
import com.taufik.themovieshow.utils.MainDispatcherRule
import com.taufik.themovieshow.utils.analytics.AnalyticsEvent
import com.taufik.themovieshow.utils.analytics.FakeAnalyticsTracker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var analyticsTracker: FakeAnalyticsTracker
    private lateinit var viewModel: AboutViewModel

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        analyticsTracker = FakeAnalyticsTracker()
        viewModel = AboutViewModel(repository, analyticsTracker)
    }

    @Test
    fun `about data comes from the repository`() {
        val sections = listOf(AboutSection.AuthorSection("Author", emptyList()))
        repository.aboutData = sections

        assertEquals(sections, viewModel.getAboutData(ApplicationProvider.getApplicationContext<Context>()))
    }

    @Test
    fun `setting a language updates the language and the changed flag`() = runTest {
        viewModel.setLanguage("in", true)

        assertEquals("in", viewModel.getCurrentLanguage())
        assertEquals("in", viewModel.languageFlow.first())
        assertTrue(repository.getLanguageChangedMessage().first())
        assertEquals(listOf(AnalyticsEvent.LanguageChange("in")), analyticsTracker.events)
    }

    @Test
    fun `setting a language without change keeps the flag off`() = runTest {
        viewModel.setLanguage("en", false)

        assertEquals("en", viewModel.getCurrentLanguage())
        assertFalse(repository.getLanguageChangedMessage().first())
        assertTrue(analyticsTracker.events.isEmpty())
    }
}
