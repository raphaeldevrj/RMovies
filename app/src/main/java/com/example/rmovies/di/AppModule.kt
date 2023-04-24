package com.example.rmovies.di

import com.example.rmovies.MoviePagingSource
import com.example.rmovies.domain.repository.MoviesDetailsRepository
import com.example.rmovies.domain.repository.MoviesHomeRepository
import com.example.rmovies.domain.repository.MoviesSearchRepository
import com.example.rmovies.domain.usecase.DetailsUseCase
import com.example.rmovies.domain.usecase.HomeUseCase
import com.example.rmovies.domain.usecase.SearchUseCase
import com.example.rmovies.presentation.viewmodel.DetailsViewModel
import com.example.rmovies.presentation.viewmodel.HomeViewModel
import com.example.rmovies.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { MoviesHomeRepository() }
        single { MoviesSearchRepository() }
        factory { MoviesDetailsRepository() }

        single { DetailsUseCase(repository = get()) }
        single { HomeUseCase(repository = get()) }
        single { SearchUseCase(repository = get()) }

        single {
            MoviePagingSource(
                homeRepository = get(),
                homeUseCase = get()
            )
        }

        viewModel { DetailsViewModel(detailsUseCase = get()) }
        viewModel { HomeViewModel(homeUseCase = get(), homeRepository = get()) }
        viewModel { SearchViewModel(searchUseCase = get()) }
    }
}