package com.taufik.themovieshow.di

import com.taufik.themovieshow.data.repository.ITheMovieShowRepository
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTheMovieShowRepository(
        repository: TheMovieShowRepository
    ): ITheMovieShowRepository
}
