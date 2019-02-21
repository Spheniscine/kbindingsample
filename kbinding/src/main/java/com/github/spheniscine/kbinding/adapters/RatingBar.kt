package com.github.spheniscine.kbinding.adapters

import android.widget.RatingBar
import com.github.spheniscine.kbinding.KBindableVar

val RatingBar.rating_kb get() = KBindableVar.adapt(
    get = ::getRating,
    set = ::setRating,
    attachListener = {
        setOnRatingBarChangeListener { _, _, _ -> it() }
    }
)