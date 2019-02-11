package com.github.spheniscine.kbindingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.Gravity
import androidx.core.view.marginTop
import com.github.spheniscine.kbinding.bindingdefs.text2way
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
                        bind(iso2way, vm::countryIso)
                    }
                    editText {
                        filters += DigitsKeyListener.getInstance()
                        inputType = InputType.TYPE_CLASS_PHONE
                        maxLength = 17
                        hint = "Phone number"
                        maxLines = 1
                        ems = 9
                        bind(text2way, vm::nationalNumber)
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
                        bind(text2way, vm::internationalNumber)
                    }
                }
            }
        }

    }
}
