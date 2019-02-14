package com.github.spheniscine.kbinding.adapters

import android.widget.NumberPicker
import com.github.spheniscine.kbinding.KBindableVar

val NumberPicker.value_kb get() = KBindableVar.adapt(
    get = ::getValue,
    set = ::setValue,
    attachListener = {
        setOnValueChangedListener{ _, _, _ -> it() }
    }
)