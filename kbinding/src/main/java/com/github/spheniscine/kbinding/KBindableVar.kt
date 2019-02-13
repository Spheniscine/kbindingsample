package com.github.spheniscine.kbinding

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

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


        /**
         * Adapts a mutable value from a non-KBindable API into a KBindableVar, so long as it has a change listener.
         * This is typically required for two-way binding.
         */
        inline fun <T> adapt(
            crossinline get: () -> T,
            crossinline set: (T) -> Unit,
            crossinline attachListener: (onChange: () -> Unit) -> Unit
        ): KBindableVar<T> = KBindableVal.adapt(get, attachListener).withSetter(set)

        inline fun <T> adapt(
            property: KMutableProperty0<T>,
            crossinline attachListener: (onChange: () -> Unit) -> Unit
        ): KBindableVar<T> = KBindableVar.adapt(property.getter, property.setter, attachListener)

    }
}



abstract class KBindableVarImpl<T> : KBindableVar<T>, KBindableValImpl<T>() {
    override val liveData = BoxedMutableLiveData<T>()

    override var value: T get() = super.value
        set(v) {
            liveData.value = Box(v)
        }
}

@Suppress("UNCHECKED_CAST")
val <T> KMutableProperty0<T>.kbvarOrNull get() = delegate as? KBindableVar<T>
val <T> KMutableProperty0<T>.kbvar get() =
    kbvarOrNull ?: throw KBindingException("Property $name not delegated to an instance of KBindableVar.")

/**
 * object marker for constructors or pseudo-constructors, to indicate late initialization of
 * a KBindableVar.
 */
object LATEINIT

/**
 * Converts a KBindableVar into an interdependent KBindableVar of a new type or format. Both a transform lambda and
 * its inverse must be provided.
 */
fun <A, B> KBindableVar<A>.convert(transform: (A) -> B, inverse: (B) -> A) =
    map(transform).withSetter { value = inverse(it) }
