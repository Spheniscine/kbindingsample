package com.github.spheniscine.kbinding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * These are observable events, that won't retrigger if an activity resubscribes to the bindable
 * (e.g. on rotation). In this they are similar to a SingleLiveEvent as described on several websites.
 *
 * Note however that if there is more than one observer observing at a time,
 * only one will be notified. I've tried a lot of things but still could not get around
 * this limitation without possibly introducing other problems - if only LiveData had some way
 * to query the number of observers
 */

interface KBindableEvent<B: Any, F: Function<Unit>> : KBindable<B, F>

abstract class AbstractKBindableEvent<B: Any, F: Function<Unit>> : KBindableEvent<B, F>, AbstractKBindable<B, F>() {
    override val liveData = MutableLiveData<B?>()
}

open class KBindableEvent0 :
    AbstractKBindableEvent<Unit, () -> Unit>() {
    override val liveData = MutableLiveData<Unit?>()

    open operator fun invoke() {
        liveData.value = Unit
    }

    override fun makeObserver(func: () -> Unit): Observer<Any?> = Observer {
        liveData.value?.let {
            liveData.value = null
            func()
        }
    }
}

open class KBindableEvent1<T> :
    AbstractKBindableEvent<Box<T>, (T) -> Unit>() {
    override val liveData = MutableLiveData<Box<T>?>()

    open operator fun invoke(arg: T) {
        liveData.value = Box(arg)
    }

    override fun makeObserver(func: (T) -> Unit): Observer<Any?> = Observer {
        liveData.value?.let {
            liveData.value = null
            func(it.value)
        }
    }
}

open class KBindableEvent2<A, B> :
    AbstractKBindableEvent<Pair<A, B>, (A, B) -> Unit>() {
    override val liveData = MutableLiveData<Pair<A, B>?>()

    open operator fun invoke(a: A, b: B) {
        liveData.value = Pair(a, b)
    }

    override fun makeObserver(func: (A, B) -> Unit): Observer<Any?> = Observer {
        liveData.value?.let {
            liveData.value = null
            func(it.first, it.second)
        }
    }
}

open class KBindableEvent3<A, B, C> :
    AbstractKBindableEvent<Triple<A, B, C>, (A, B, C) -> Unit>() {
    override val liveData = MutableLiveData<Triple<A, B, C>?>()

    open operator fun invoke(a: A, b: B, c: C) {
        liveData.value = Triple(a, b, c)
    }

    override fun makeObserver(func: (A, B, C) -> Unit): Observer<Any?> = Observer {
        liveData.value?.let {
            liveData.value = null
            func(it.first, it.second, it.third)
        }
    }
}