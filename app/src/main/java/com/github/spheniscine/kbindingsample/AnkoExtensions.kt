package com.github.spheniscine.kbindingsample

import android.text.InputFilter
import android.view.View
import android.view.ViewManager
import android.widget.TextView
import com.rilixtech.CountryCodePicker
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.internals.AnkoInternals.NO_GETTER
import org.jetbrains.anko.internals.AnkoInternals.noGetter
import kotlin.DeprecationLevel.ERROR

inline fun ViewManager.countryCodePicker(init: CountryCodePicker.() -> Unit = {}): CountryCodePicker {
    return ankoView({ CountryCodePicker(it) }, theme = 0, init = init)
}

var TextView.maxLength: Int
    @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
    set(value) { filters += InputFilter.LengthFilter(value) }

var View.onClick: () -> Unit
    @Deprecated(NO_GETTER, level = ERROR) get() = noGetter()
    set(value) { setOnClickListener { value() } }