package com.github.spheniscine.kbinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

/** Interface for all KBindables where values can be read from **/
interface KBindableVal<T> : KBindable<Box<T>, (T) -> Unit> {

    /**
     * The value in the backing LiveData is boxed to make it null-safe, and allow distinguishing
     * between KBindableVal<T> and KBindableVal<T?>.
     */
    override val liveData: BoxedLiveData<T>

    val value: T

    // Needed to allow Kotlin to use this as a property delegate
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T

    val initialized: Boolean

    /**
     * The equivalent of Transformations.map for liveData, this takes any KBindableVal and
     * returns a new KBindableVal whose value depends on the original's value, and
     * modified by [transform]
     */
    fun <N> map(transform: (T) -> N) : KBindableVal<N>

    fun toStringVal() : KBindableVal<String>

    companion object {
        /**
         * Wraps an arbitrary LiveData to be usable as a delegate. Note however, that the
         * resulting property is always nullable, and [initialized] will always return true
         * as there is no way to tell whether the LiveData has actually been initialized without
         * possibly negatively affecting it
         */
        fun <T> wrap(liveData: LiveData<T>): KBindableVal<T?> =
                wrapBoxed(liveData.toBoxed())

        /**
         * If you did already box the LiveData, you can use this instead. [initialized]
         * would depend on whether the LiveData contains a nulled box or not so be careful what
         * you do with it
         */
        fun <T> wrapBoxed(liveData: BoxedLiveData<T>): KBindableVal<T> =
                object : KBindableValImpl<T>() {
                    override val liveData: BoxedLiveData<T> = liveData
                }
    }
}

abstract class KBindableValImpl<T> : KBindableVal<T>, KBindableImpl<Box<T>, (T) -> Unit>() {

    override val initialized get() = liveData.value != null

    override val value: T get() {
        val box = liveData.value
        if(box != null) return box.value
        else throw UninitializedPropertyAccessException()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override fun makeObserver(func: (T) -> Unit): Observer<Any?> = Observer { func(value) }

    override fun <N> map(transform: (T) -> N): KBindableVal<N> =
        KBindableVal.wrapBoxed(
            Transformations.map(this.liveData) {
                Box(transform(value))
            }.apply { kick() }
        )

    override fun toStringVal(): KBindableVal<String> = map { it.toString() }
}

@Suppress("UNCHECKED_CAST")
val <T> KProperty0<T>.kbvalOrNull get() = delegate as? KBindableVal<T>
val <T> KProperty0<T>.kbval get() =
    try { kbvalOrNull!! }
    catch(e: Exception) { throw KBindingException("Property $name not delegated to an instance of KBindableVal.") }

fun <T> KBindableVal<T>.getOrNull() =
    ::value.getOrNull()
fun <R, T:R> KBindableVal<T>.getOrElse(onFailure: (Throwable) -> R)=
    ::value.getOrElse(onFailure)
fun <R, T:R> KBindableVal<T>.getOrDefault(default: R) =
    ::value.getOrDefault(default)