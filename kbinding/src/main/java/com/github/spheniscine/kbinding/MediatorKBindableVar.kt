package com.github.spheniscine.kbinding

import kotlin.reflect.KProperty0

interface MediatorKBindableVar<T> : KBindableVar<T> {

    fun <S> addSource(source: KBindableVal<S>, onChanged: (S) -> Unit)
    fun <S> removeSource(toRemote: KBindableVal<S>)

    // attempts to grab the KBindables from the property reference. Will throw an exception
    // if the property isn't delegated to a KBindable
    fun <S> addSource(source: KProperty0<S>, onChanged: (S) -> Unit)
    fun <S> removeSource(toRemote: KProperty0<S>)

    companion object {
        /** pseudo-constructor factory method */
        operator fun <T> invoke(initialValue: T) : MediatorKBindableVar<T> =
            object: MediatorKBindableVarImpl<T>() {
                init { value = initialValue }
            }

        /** alternative factory method that leaves the KBindableVar uninitialized */
        operator fun <T> invoke(lateInitMarker: LATEINIT) : MediatorKBindableVar<T> =
            object: MediatorKBindableVarImpl<T>() {}
    }
}

/**
 * Convenience function to add several sources at once. Note that the function accepts no input;
 * it is assumed you can access the sources easily
 */

fun <T> MediatorKBindableVar<T>.addSources(vararg sources: KProperty0<*>, onChanged: () -> Unit){
    for(source in sources) {
        addSource(source) { onChanged() }
    }
}

abstract class MediatorKBindableVarImpl<T> : MediatorKBindableVar<T>, KBindableVarImpl<T>() {
    override val liveData = BoxedMediatorLiveData<T>()

    override fun <S> addSource(source: KBindableVal<S>, onChanged: (S) -> Unit) =
            liveData.addSource(source.liveData) {
                onChanged(source.value)
            }

    override fun <S> removeSource(toRemote: KBindableVal<S>) =
            liveData.removeSource(toRemote.liveData)

    override fun <S> addSource(source: KProperty0<S>, onChanged: (S) -> Unit) =
            addSource(source.kbval, onChanged)

    override fun <S> removeSource(toRemote: KProperty0<S>) =
            removeSource(toRemote.kbval)
}

