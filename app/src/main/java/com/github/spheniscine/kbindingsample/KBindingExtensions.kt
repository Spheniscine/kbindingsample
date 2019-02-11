package com.github.spheniscine.kbindingsample

import com.github.spheniscine.kbinding.bindingdefs.K2WayBinder
import com.rilixtech.CountryCodePicker

val CountryCodePicker.iso2way get() = K2WayBinder(
    get = ::getSelectedCountryNameCode,
    set = ::setCountryForNameCode,
    attachListener = { setOnCountryChangeListener { it() } }
)