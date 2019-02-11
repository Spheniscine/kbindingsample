package com.github.spheniscine.kbindingsample

import android.text.InputFilter
import android.view.ViewManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.rilixtech.CountryCodePicker
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.internals.AnkoInternals

inline fun ViewManager.countryCodePicker(init: CountryCodePicker.() -> Unit = {}): CountryCodePicker {
    return ankoView({ CountryCodePicker(it) }, theme = 0, init = init)
}

var TextView.maxLength: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) { filters += InputFilter.LengthFilter(value) }