package com.github.spheniscine.kbindingsample

import com.github.spheniscine.kbinding.*

class TwoWayBindingExampleViewModel : BaseViewModel() {
    var countryIso by KBindableVar<String>(LATEINIT)
    var nationalNumber by KBindableVar("")

    var internationalNumber by KBindableVar("")

    private var e164Number by
        ::internationalNumber.kbmap { "+$it" }
            .withSetter { internationalNumber = it.removePrefix("+") }

    val phoneDownEnabled by
        setOf(::countryIso, ::nationalNumber).kbmerge {
            runCatching { isValidPhoneNumber(nationalNumber, countryIso) }.getOrDefault(false)
        }
    fun phoneDownClicked() {
        e164Number = fullPhoneNumber(nationalNumber, countryIso) ?: return
    }

    val phoneUpEnabled by ::e164Number.kbmap { isValidPhoneNumber(it, null) }
    fun phoneUpClicked() {
        val ni = getNationalNumberAndIso(e164Number) ?: return
        nationalNumber = ni.nationalNumber
        countryIso = ni.countryIso
    }

}