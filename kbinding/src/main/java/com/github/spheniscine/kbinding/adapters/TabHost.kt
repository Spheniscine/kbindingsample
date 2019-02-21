package com.github.spheniscine.kbinding.adapters

import android.widget.TabHost
import com.github.spheniscine.kbinding.KBindableVar

val TabHost.currentTab_kb get() = KBindableVar.adapt(
    get = ::getCurrentTab,
    set = ::setCurrentTab,
    attachListener = {
        setOnTabChangedListener { it() }
    }
)