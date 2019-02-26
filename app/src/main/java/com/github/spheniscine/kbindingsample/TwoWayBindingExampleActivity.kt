package com.github.spheniscine.kbindingsample

import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.Gravity
import android.view.View
import com.github.spheniscine.kbinding.adapters.text_kb
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

                view().lparams(width = 0, height = SPACING * 2)

                linearLayout {
                    topPadding = SPACING
                    gravity = Gravity.CENTER
                    val pinArray = List(6) {
                        editText {
                            inputType = InputType.TYPE_CLASS_NUMBER
                            maxLength = 1
                            maxLines = 1
                            ems = 2
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textColor = 0.opaque
                        }
                    }
                    bindCharEditTextArray(pinArray, vm::pin)
                }

                textView("⇵") { gravity = Gravity.CENTER }

                linearLayout {
                    gravity = Gravity.CENTER
                    topPadding = SPACING
                    editText {
                        inputType = InputType.TYPE_CLASS_NUMBER
                        maxLength = 6
                        maxLines = 1
                        ems = 6
                        bind2(text_kb, vm::pin)
                    }
                }
            }
        }

    }
}
