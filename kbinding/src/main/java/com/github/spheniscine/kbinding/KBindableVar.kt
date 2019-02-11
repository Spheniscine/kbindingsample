package com.github.spheniscine.kbinding

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/** Interface for all KBindables where values can be read from and set to **/
interface KBindableVar<T> : KBindableVal<T> {
    override var value: T

    infix fun setIfNot(new: T) = ::value setIfNot new

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }

    companion object {
        /** pseudo-constructor factory method */
        operator fun <T> invoke(initialValue: T) : KBindableVar<T> =
            object: KBindableVarImpl<T>() {
                init { value = initialValue }
            }

        /**
         * alternative factory method that leaves the KBindableVar uninitialized.
         * Like native Kotlin lateinit variables, be careful when you access this kind of variable.
         */
        operator fun <T> invoke(lateInitMarker: LATEINIT) : KBindableVar<T> =
            object: KBindableVarImpl<T>() {}

    }
}



abstract class KBindableVarImpl<T> : KBindableVar<T>, KBindableValImpl<T>() {
    override val liveData = BoxedMutableLiveData<T>()

    override var value: T get() = super.value
        set(v) {
            liveData.value = Box(v)
        }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }
}

@Suppress("UNCHECKED_CAST")
val <T> KMutableProperty0<T>.kbvarOrNull get() = delegate as? KBindableVar<T>
val <T> KMutableProperty0<T>.kbvar get() =
    try { kbvarOrNull!! }
    catch(e: Exception) { throw KBindingException("Property $name not delegated to an instance of KBindableVar.") }

/**
 * object marker for constructors or pseudo-constructors, to indicate late initialization of
 * a KBindableVar.
 */
object LATEINIT
