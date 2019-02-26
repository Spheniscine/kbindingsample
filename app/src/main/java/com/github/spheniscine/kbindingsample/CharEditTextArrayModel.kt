package com.github.spheniscine.kbindingsample

import android.view.KeyEvent
import android.widget.EditText
import com.github.spheniscine.kbinding.*
import com.github.spheniscine.kbinding.adapters.text_kb
import kotlin.reflect.KMutableProperty0

class CharEditTextArrayModel(val size: Int) {
    val lastIndex = size - 1
    val indices = 0 .. lastIndex

    var enabled by KBindableVar(true).updater { update() }

    val editTextContents = KBindableVarArray(List(size) {
        KBindableVar("").updater { update() }
    })

    var value: String by editTextContents.merge {
        it.joinToString("")
    }.withSetter {
        for(i in indices) {
            editTextContents[i] = if(i < it.length) it[i].toString() else ""
        }
        update(it)
    }


    val editTextEnabled = KBindableVarArray(size) { enabled && it == 0 }

    val editTextRequestFocus = List(size) { KBindableEvent0() }

    // to be "reverse bound" to the edit texts so that we can tell if they are focused
    val editTextFocused = KBindableVarArray(size) { false }

    private fun update(value: String = this.value) {
        if(!enabled) {
            for(i in indices) editTextEnabled[i] = false
        } else {
            val activeIndex = minOf(lastIndex, value.length)
            editTextEnabled[activeIndex] = true
            if(editTextFocused.any { it }) editTextRequestFocus[activeIndex]()
            for(i in indices) if(i != activeIndex) editTextEnabled[i] = false
        }
    }

    fun onBackspace(index: Int): Boolean {
        if(editTextContents[index].isNotEmpty()) {
            editTextContents[index] = ""
            return true
        }

        if(index > 0) {
            editTextContents[index - 1] = ""
            return true
        }
        return false
    }
}

fun KBindingClient.bindCharEditTextArray(editTexts: List<EditText>, value: KMutableProperty0<String>,
                                         enabled: KMutableProperty0<Boolean>? = null) {
    val model = CharEditTextArrayModel(editTexts.size)
    editTexts.forEachIndexed { index, editText ->
        bind2(editText.text_kb, model.editTextContents.kbvars[index])
        bind(editText::setEnabled, model.editTextEnabled.kbvals[index])
        bind(model.editTextFocused.kvars[index], editText.isFocused_kb)
        model.editTextRequestFocus[index] calls { editText.requestFocus() }

        editText.setOnKeyListener { _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                model.onBackspace(index)
            } else false
        }

        bind2(model::value, value)
        enabled?.let { bind(model::enabled, it) }
    }
}