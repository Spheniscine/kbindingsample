package com.github.spheniscine.kbindingsample

import android.view.View
import com.github.spheniscine.kbinding.KBindableVal
import com.github.spheniscine.kbinding.KBindableVar
import com.rilixtech.CountryCodePicker

val CountryCodePicker.iso_kb: KBindableVar<String> get() = KBindableVar.adapt(
    get = ::getSelectedCountryNameCode,
    set = ::setCountryForNameCode,
    attachListener = { setOnCountryChangeListener { it() } }
)

val View.isFocused_kb: KBindableVal<Boolean> get() = KBindableVal.Companion.adapt(
    get = ::isFocused,
    attachListener = { setOnFocusChangeListener { _, _ -> it() }}
)