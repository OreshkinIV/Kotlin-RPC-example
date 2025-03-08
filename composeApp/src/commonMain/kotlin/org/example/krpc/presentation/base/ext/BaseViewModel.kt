package org.example.krpc.presentation.base.ext

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<T: Any>: ViewModel() {

    protected val _events = MutableSharedFlow<T>()
    val events = _events.asSharedFlow()
}