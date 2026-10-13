package com.taufik.themovieshow.ui.main

import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.utils.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `language changed message follows the repository`() = runTest {
        assertFalse(viewModel.languageChangedMessage.first())

        repository.setLanguageChangedMessage(true)

        assertTrue(viewModel.languageChangedMessage.first())
    }

    @Test
    fun `clearing the flag resets the language changed message`() = runTest {
        repository.setLanguageChangedMessage(true)

        viewModel.clearLanguageChangedFlag()

        assertFalse(viewModel.languageChangedMessage.first())
    }
}
