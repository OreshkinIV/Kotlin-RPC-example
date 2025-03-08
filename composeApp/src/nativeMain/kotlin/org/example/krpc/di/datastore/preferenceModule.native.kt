package org.example.krpc.di.datastore

import org.example.krpc.data.preferences.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferenceModule: Module = module {
    single { createDataStore(null) }
}