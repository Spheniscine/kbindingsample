package com.github.spheniscine.kbinding.adapters

import android.widget.RadioGroup
import com.github.spheniscine.kbinding.KBindableVar

val RadioGroup.checkedButton_kb get() = KBindableVar.adapt(
    get = ::getCheckedRadioButtonId,
    set = ::check,
    attachListener = {
        setOnCheckedChangeListener { _, _ -> it() }
    }
)