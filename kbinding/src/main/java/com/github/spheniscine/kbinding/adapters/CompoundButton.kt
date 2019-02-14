package com.github.spheniscine.kbinding.adapters

import android.widget.CompoundButton
import com.github.spheniscine.kbinding.KBindableVar

val CompoundButton.checked_kb get() = KBindableVar.adapt(
    get = ::isChecked,
    set = ::setChecked,
    attachListener = {
        setOnCheckedChangeListener{ _, _ -> it() }
    }
)