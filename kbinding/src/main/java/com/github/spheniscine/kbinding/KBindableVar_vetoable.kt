package com.github.spheniscine.kbinding

/**
 * Extends a given KBindableVar into a "vetoable".
 *
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
fun <T> KBindableVar<T>.vetoable(
    beforeSet: (old: T?, intent: T) -> T = {_, intent -> intent },
    afterSet: (old: T?, intent: T, new: T) -> Unit = {_,_,_->}) =
        withSetter { intent ->
            val old = getOrNull()
            val new = beforeSet(old, intent)
            value = new
            afterSet(old, intent, new)
        }

/**
 * "Updaters" are simplified forms of vetoables that simply call a function (typically a private data
 * integrity function in the view model) after it is set.
 */
fun <T> KBindableVar<T>.updater(initialValue: T, afterSet: () -> Unit) =
    vetoable(afterSet = { _, _, _ -> afterSet() })