package com.github.spheniscine.kbinding.adapters

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.github.spheniscine.kbinding.KBindableVar

val TextView.text_kb get() = KBindableVar.adapt(
    get = { text.toString() },
    set = ::setText,
    attachListener = { update ->
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { update() }
        })
    }
)