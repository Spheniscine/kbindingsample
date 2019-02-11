package com.github.spheniscine.kbinding

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Base KBindable interface that includes some shared common functions.
 *
 * B represents the type of the backing LiveData - note that this type must not be nullable, but
 * the values you'll work with from the LiveData will all be nullable; while F represents the function
 * type that observers should input (the return type is always Unit)
 */
interface KBindable<B: Any, F: Function<Unit>> {
    val liveData: LiveData<B?>

    /**
     * There are no reference to observers. If you need more control over the state of the
     * internal observers, use [ControllableLifecycleOwner] instead.
     */
    fun observe(owner: LifecycleOwner, func: F)

    fun observeForever(func: F)

    fun removeObservers(owner: LifecycleOwner)

    val hasActiveObservers: Boolean
    val hasObservers: Boolean
}

abstract class KBindableImpl<B: Any, F: Function<Unit>>: KBindable<B, F> {
    protected abstract fun makeObserver(func: F) : Observer<in B?>

    final override fun observe(owner: LifecycleOwner, func: F) {
        val observer = makeObserver(func)
        liveData.observe(owner, observer)
    }

    final override fun observeForever(func: F) {
        val observer = makeObserver(func)
        liveData.observeForever(observer)
    }

    override fun removeObservers(owner: LifecycleOwner) = liveData.removeObservers(owner)

    override val hasActiveObservers: Boolean get() = liveData.hasActiveObservers()
    override val hasObservers: Boolean get() = liveData.hasObservers()
}