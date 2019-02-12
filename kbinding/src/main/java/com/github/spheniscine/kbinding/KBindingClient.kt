package com.github.spheniscine.kbinding

import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * Have your activites and fragments extend this interface to unlock these useful extension functions.
 */
interface KBindingClient : LifecycleOwner {
    fun <B: Any, F: Function<Unit>> KBindable<B, F>.observe(func: F) =
        this.observe(this@KBindingClient, func)


    /**
     * attach a function to any property delegated by a KBindableVal instance
     * Will throw an exception if used on a property not delegated by a KBindableVal - to avoid this
     * you should hide any properties that you don't want to bind
     */
    fun <T> KProperty0<T>.onSet(func: (T) -> Unit) {
        this.kbval.observe(func)
    }

    fun <T> KBindableVal<T>.onSet(func: (T) -> Unit) {
        this.observe(func)
    }

    /**
     * alias of onSet, which allows for a really nice syntax e.g.
     * vm::name sets tvName::setText
     */
    infix fun <T> KProperty0<T>.sets(viewFunc: (T) -> Unit) = onSet(viewFunc)
    infix fun <T> KProperty0<T>.sets(viewProp: KMutableProperty0<in T>) = onSet(viewProp.setter)
    infix fun <T> KBindableVal<T>.sets(viewFunc: (T) -> Unit) = onSet(viewFunc)
    infix fun <T> KBindableVal<T>.sets(viewProp: KMutableProperty0<in T>) = onSet(viewProp.setter)

    /**
     * Ditto for events - e.g. vm.runAnimation calls ::runAnimation
     * Knowing when to use . vs :: can be a potential pitfall, but hopefully the types
     * are a big hint
     */
    infix fun <F: Function<Unit>> KBindableEvent<*, F>.calls(func: F) = observe(func)

    /**
     * binding functions that are in the reverse order of [sets]. Usage example:
     * bind(tvName::setText, vm::name)
     *
     * Most handy if you're using Anko Layouts, as the view name can be omitted if declared within
     * the DSL.
     */
    fun <T> bind(viewFunc: (T) -> Unit, bindable: KProperty0<T>) = bindable sets viewFunc
    fun <T> bind(viewProp: KMutableProperty0<in T>, bindable: KProperty0<T>) = bindable sets viewProp
    fun <T> bind(viewFunc: (T) -> Unit, bindable: KBindableVal<T>) = bindable sets viewFunc
    fun <T> bind(viewProp: KMutableProperty0<in T>, bindable: KBindableVal<T>) = bindable sets viewProp


    /**
     * Does a two-way binding from a view to a data source. Note that order is important; if the two bindables
     * have different values when this function is called, [dataBindable] will take priority.
     *
     * Android and third-party view objects are unlikely to have a KBindableVar (or a property delegated to
     * KBindableVar) handy to bind with. As such, extension properties need to be defined using [KBindableVar.adapt].
     * See the bindingdefs package for examples; the plan is to include every Android property useful for
     * two-way-binding there.
     */
    fun <T> bind2(viewBindable: KBindableVar<T>, dataBindable: KBindableVar<T>) {
        dataBindable.observe(viewBindable::setIfNot)
        viewBindable.observe(dataBindable::setIfNot)
    }
    fun <T> bind2(viewBindable: KMutableProperty0<T>, dataBindable: KBindableVar<T>) = bind2(viewBindable.kbvar, dataBindable)
    fun <T> bind2(viewBindable: KBindableVar<T>, dataBindable: KMutableProperty0<T>) = bind2(viewBindable, dataBindable.kbvar)
    fun <T> bind2(viewBindable: KMutableProperty0<T>, dataBindable: KMutableProperty0<T>) = bind2(viewBindable.kbvar, dataBindable.kbvar)
}