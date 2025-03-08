package org.example.krpc.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return createDataStore(
        producePath = { "path/$dataStoreFileName" }
    )
}