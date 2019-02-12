package com.github.spheniscine.kbindingsample

import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.Gravity
import com.github.spheniscine.kbinding.bindingdefs.text_kb
import org.jetbrains.anko.*
import org.koin.android.viewmodel.ext.viewModel

class TwoWayBindingExampleActivity : BaseActivity() {

    private val vm: TwoWayBindingExampleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val SPACING = dip(8)

        scrollView {
            isFillViewport = true
            verticalLayout {
                linearLayout {
                    topPadding = SPACING
                    gravity = Gravity.CENTER
                    countryCodePicker {
                        bind2(iso_kb, vm::countryIso)
                    }
                    editText {
                        filters += DigitsKeyListener.getInstance()
                        inputType = InputType.TYPE_CLASS_PHONE
                        maxLength = 17
                        hint = "Phone number"
                        maxLines = 1
                        ems = 9
                        bind2(text_kb, vm::nationalNumber)
                    }
                }

                linearLayout {
                    topPadding = SPACING
                    gravity = Gravity.CENTER
                    button("↓") {
                        onClick = vm::phoneDownClicked
                        bind(::setEnabled, vm::phoneDownEnabled)
                    }
                    view().lparams(width = SPACING, height = 0)
                    button("↑") {
                        onClick = vm::phoneUpClicked
                        bind(::setEnabled, vm::phoneUpEnabled)
                    }
                }

                linearLayout {
                    topPadding = SPACING
                    gravity = Gravity.CENTER
                    textView("+")
                    editText {
                        filters += DigitsKeyListener.getInstance()
                        inputType = InputType.TYPE_CLASS_PHONE
                        maxLength = 17
                        hint = "Phone number (international)"
                        maxLines = 1
                        ems = 12
                        bind2(text_kb, vm::internationalNumber)
                    }
                }
            }
        }

    }
}
