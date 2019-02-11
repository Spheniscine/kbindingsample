package com.github.spheniscine.kbinding.bindingdefs

import androidx.lifecycle.LifecycleOwner
import com.github.spheniscine.kbinding.KBindableVar
import com.github.spheniscine.kbinding.setIfNot
import kotlin.reflect.KMutableProperty0

/**
 * To define your own two-way binding adapters, define an extension property for the relevant
 * view object, and have it return an object implementing K2WayBinder, overriding the [attr]
 * property to teach the two-way binder how to get and set the relevant attribute, and overriding
 * the [attachListener] function to teach the two-way binder how to attach a listener
 * to the view object for an attribute change.
 *
 * Alternatively, you can use the pseudo-constructor to create a K2WayBinder object using lambdas
 * or function references.
 *
 * For those experienced with Data Binding library:
 * [attr]'s getter is similar to the @InverseBindingAdapter,
 * [attr]'s setter is similar to the @BindingAdapter,
 * [attachListener] is similar to the @BindingAdapter for xxxAttrChanged.
 * However, unlike DBL, K2WayBinder definitions are infinite-loop safe, so you don't need an equality check
 * in the setter; in fact doing so is discouraged.
 */
interface K2WayBinder<D> {
    var attr: D

    fun attachListener(update: () -> Unit)

    fun bind(owner: LifecycleOwner, kbvar: KBindableVar<D>) {
        val update = { kbvar setIfNot attr }
        attachListener(update)
        kbvar.observe(owner, ::attr::setIfNot)
        if(!kbvar.initialized) update() // automatically updates the source kbvar if it's not initialized
    }

    companion object {
        inline operator fun <D> invoke(
            crossinline get: () -> D,
            crossinline set: (D) -> Unit,
            crossinline attachListener: (() -> Unit) -> Unit
        ) = object : K2WayBinder<D> {
            override var attr: D
                get() = get()
                set(value) { set(value) }

            override fun attachListener(update: () -> Unit) = attachListener(update)
        }

        inline operator fun <D> invoke(
            attr: KMutableProperty0<D>,
            crossinline attachListener: (() -> Unit) -> Unit
        ) = invoke(attr.getter, attr.setter, attachListener)
    }
}


