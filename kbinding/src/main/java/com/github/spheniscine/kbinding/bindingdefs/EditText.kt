package com.github.spheniscine.kbinding.bindingdefs

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.github.spheniscine.kbinding.KBindableVar

val EditText.text_kb get() = KBindableVar.retrofit(
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