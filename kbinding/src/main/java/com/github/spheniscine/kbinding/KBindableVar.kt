package com.github.spheniscine.kbinding

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/** Interface for all KBindables where values can be read from and set to **/
interface KBindableVar<T> : KBindableVal<T> {
    override var value: T

    // Needed to allow Kotlin to use this as a property delegate
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

    infix fun setIfNot(new: T)

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

        fun <T> vetoable(lateInitMarker: LATEINIT,
                         beforeSet: (old: T?, intent: T) -> T = {_, intent -> intent },
                         afterSet: (old: T?, intent: T, new: T) -> Unit): KBindableVar<T> =
            object : KBindableVarImpl<T>() {
                override var value get() = super.value
                    set(intent) {
                        val old = getOrNull()
                        val new = beforeSet(old, intent)
                        super.value = new
                        afterSet(old, intent, new)
                    }
            }

        /**
         * "Vetoables" allow you to intercept any attempt to set the KBindableVar value; useful for
         * two-way bindings.
         *
         * @param beforeSet accepts the current/"old" value (null if uninitialized), and the intended
         * value. Return value should be what *should* be set. The default will just accept the
         * intended value.
         *
         * @param afterSet accepts the old value, the intended value, and the new value actually
         * set (the return value of [beforeSet]). Default will do nothing.
         */
        fun <T> vetoable(initialValue: T,
                         beforeSet: (old: T?, intent: T) -> T = {_, intent -> intent },
                         afterSet: (old: T?, intent: T, new: T) -> Unit = {_,_,_->}) =
            object : KBindableVarImpl<T>() {
                init { super.value = initialValue }
                override var value get() = super.value
                    set(intent) {
                        val old = getOrNull()
                        val new = beforeSet(old, intent)
                        super.value = new
                        afterSet(old, intent, new)
                    }
            }

        fun <T> updater(lateInitMarker: LATEINIT, afterSet: () -> Unit) =
            vetoable<T>(LATEINIT, afterSet = {_, _, _ -> afterSet()})

        /**
         * "Updaters" are simplified forms of vetoables that simply call a function after it
         * is set.
         */
        fun <T> updater(initialValue: T, afterSet: () -> Unit) =
            vetoable(initialValue, afterSet = {_, _, _ -> afterSet()})

    }
}



abstract class KBindableVarImpl<T> : KBindableVar<T>, KBindableValImpl<T>() {
    override val liveData = BoxedMutableLiveData<T>()

    override var value: T get() = super.value
        set(v) {
            liveData.value = Box(v)
        }

    override fun setIfNot(new: T) { if(!initialized || value != new) value = new }

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
