package com.github.spheniscine.kbinding


data class Box<out T>(val value: T) {
    /** returns a new [Box] whose value is modified by the [transform] function */
    inline fun <N> map(transform: (T) -> N) = Box(transform(value))
}