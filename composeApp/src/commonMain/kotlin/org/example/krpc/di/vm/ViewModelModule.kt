package org.example.krpc.di.vm

import org.example.krpc.presentation.auth.AuthViewModel
import org.example.krpc.presentation.home.HomeViewModel
import org.example.krpc.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule
    get() = module {
        viewModel { AuthViewModel(get(), get(), get()) }
        viewModel { SplashViewModel(get()) }
        viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    }