package org.example.krpc.di

import org.example.krpc.di.network.ktorClientModule
import org.example.krpc.di.auth.authModule
import org.example.krpc.di.datastore.preferenceModule
import org.example.krpc.di.user.userModule
import org.example.krpc.di.vm.viewModelModule

val appModules
    get() = listOf(
        authModule,
        ktorClientModule,
        userModule,
        viewModelModule,
        preferenceModule
    )