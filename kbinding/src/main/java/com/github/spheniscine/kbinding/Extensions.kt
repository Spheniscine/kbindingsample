package com.github.spheniscine.kbinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/**
 * convenience functions for Kotlin properties - these are really handy for any use-case that makes
 * heavy use of lateinits, delegates, and overrided getters/setters, not just KBinding
 *
 * usage example: `::greeting setIfNot "Hello"`
 *
 * [setIfNot] wraps the getter in a try-block, and will set the [value] if an exception was thrown
 * or if the values aren't equal.
 */
infix fun <T> KMutableProperty0<T>.setIfNot(value: T) {
    val equal = runCatching { get() == value }.getOrDefault(false)

    if(!equal) set(value)
}

fun <T> KProperty0<T>.getOrNull() =
    runCatching(getter).getOrNull()
inline fun <R, T:R> KProperty0<T>.getOrElse(onFailure: (Throwable) -> R) =
    runCatching(getter).getOrElse(onFailure)
fun <R, T:R> KProperty0<T>.getOrDefault(default: R) =
    runCatching(getter).getOrElse { default }

val <T> KProperty0<T>.delegate : Any? get() {
    isAccessible = true
    return getDelegate()
}

/**
 * Allows you to define simple templates/transforms for an observed value. e.g.
 * viewModel::price.kbmap { "Price: $it" } sets tvPrice::setText
 *
 * Alternatively, you can have the viewModel delegate one property to another, e.g.
 * val priceText: String by ::price.kbmap { "Price: $it" }
 */
fun <A, B> KProperty0<A>.kbmap(transform: (A) -> B) = kbval.map(transform)

/**
 * Simple mapping from an arbitrary observed value to a string.
 */
fun <T> KProperty0<T>.kbstring() = kbval.toStringKBVal()

/**
 * For two-way conversion; both the transform and its inverse must be defined. See [KBindableVar.convert]
 */
inline fun <A, B> KMutableProperty0<A>.kbconvert(
    noinline transform: (A) -> B, crossinline inverse: (B) -> A
) =
    kbvar.convert(transform, inverse)

/**
 * Similar to [kbmap] with multiple properties. Internally uses [MediatorKBindableVar]
 * @receiver A set of properties (must be delegated by KBindables) that the merged property depends on
 * @param result The function that generates the result. Note that it has no input; it is assumed
 * you can easily access the properties.
 */
fun <R> Iterable<KProperty0<*>>.kbmerge(result: () -> R): KBindableVal<R> {
    val sources = this
    return object : MediatorKBindableVarImpl<R>() {
        init {
            for (source in sources) {
                addSource(source) { value = result() }
            }
            liveData.kick()
        }
    }
}

/**
 * Easily convert a KBindableVal and KBindableVar to a property or a mutable property object
 * for compatibility ("k" for Kotlin, naturally)
 *
 * You can chain kbval and kval forever, but that's probably a bad idea :P
 */
val <T> KBindableVal<T>.kval : KProperty0<T> get() {
    val o = object { val p by this@kval }
    return o::p
}

val <T> KBindableVar<T>.kvar : KMutableProperty0<T> get() {
    val o = object { var p by this@kvar }
    return o::p
}

/**
 * Used to force "lazy" LiveData instances like the one returned from [Transformations.map] to
 * update. This reduces the chance of uninitialized property exceptions from KBindableVal
 */
internal fun <T> LiveData<T>.kick() {
    if(hasActiveObservers()) return
    val owner = ControllableLifecycleOwner()
    observe(owner, Observer {})
    owner.destroy()
}
