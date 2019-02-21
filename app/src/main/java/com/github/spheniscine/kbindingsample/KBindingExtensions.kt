package com.github.spheniscine.kbindingsample

import com.github.spheniscine.kbinding.KBindableVar
import com.rilixtech.CountryCodePicker

val CountryCodePicker.iso_kb: KBindableVar<String> get() = KBindableVar.adapt(
    get = ::getSelectedCountryNameCode,
    set = ::setCountryForNameCode,
    attachListener = { setOnCountryChangeListener { it() } }
)