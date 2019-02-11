package com.github.spheniscine.kbinding

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData

/**
 * Simple controllable LifecycleOwner implementation. This is a safer way to manipulate LiveData
 * activity states than by using [LiveData.observeForever] and [LiveData.removeObserver] as
 * subclasses of LiveData may wrap the observer making the reference useless.
 */
class ControllableLifecycleOwner(initialState: Lifecycle.State = Lifecycle.State.RESUMED)
    : LifecycleOwner {
    val lifecycle = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycle

    var state get() = lifecycle.currentState
        set(value) = lifecycle.markState(value)

    init {
        state = initialState
    }

    fun destroy() {
        state = Lifecycle.State.DESTROYED
    }
}