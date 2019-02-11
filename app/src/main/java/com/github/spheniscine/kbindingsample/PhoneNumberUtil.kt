package com.github.spheniscine.kbindingsample

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

data class NationalNumberAndIso (var nationalNumber : String, var countryIso : String)

private val phoneUtil by lazy { PhoneNumberUtil.getInstance() }

private fun parse(mobileNumber : String, countryIso : String?) : Phonenumber.PhoneNumber? =
    try { phoneUtil.parse(mobileNumber, countryIso) }
    catch(e : Exception) { null }

/** returns the phone number in E164 format **/
fun e164PhoneNumber(mobileNumber : String, countryIso : String?) : String? {
    val parsed = parse(mobileNumber, countryIso) ?: return null
    return phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)
}

fun isValidPhoneNumber(mobileNumber: String, countryIso: String?) : Boolean {
    val parsed = parse(mobileNumber, countryIso) ?: return false
    return phoneUtil.isValidNumber(parsed)
}

/** split an E164 number into nationally significant number and country ISO **/
fun getNationalNumberAndIso(e164PhoneNumber : String) : NationalNumberAndIso? {
    val parsed = parse(e164PhoneNumber, "") ?: return null
    val nationalNumber = parsed.nationalNumber.toString()
    val countryIso = phoneUtil.getRegionCodeForNumber(parsed)
    return NationalNumberAndIso(nationalNumber, countryIso)
}