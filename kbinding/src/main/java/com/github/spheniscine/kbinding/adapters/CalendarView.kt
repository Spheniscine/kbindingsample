package com.github.spheniscine.kbinding.adapters

import android.widget.CalendarView
import com.github.spheniscine.kbinding.KBindableVar

val CalendarView.date_kb get() = KBindableVar.adapt(
    get = ::getDate,
    set = ::setDate,
    attachListener = {
        setOnDateChangeListener{ _, _, _, _ -> it() }
    }
)