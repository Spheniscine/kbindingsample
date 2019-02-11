package com.github.spheniscine.kbindingsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.Gravity
import androidx.core.view.marginTop
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val SPACING = dip(8)

        scrollView {
            isFillViewport = true
            verticalLayout {
                linearLayout {
                    topPadding = SPACING
                    gravity = Gravity.CENTER
                    countryCodePicker()
                    editText {
                        filters += DigitsKeyListener.getInstance()
                        inputType = InputType.TYPE_CLASS_PHONE
                        maxLength = 17
                        hint = "Phone number"
                        maxLines = 1
                        ems = 9
                    }
                }

                linearLayout {
                    topPadding = SPACING
                    gravity = Gravity.CENTER
                    button("↓")
                    view().lparams(width = SPACING, height = 0)
                    button("↑")
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
                    }
                }
            }
        }

    }
}
