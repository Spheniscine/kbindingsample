package com.github.spheniscine.kbinding

interface KBindableValArray<T> {
    val kbvals : List<KBindableVal<T>>

    operator fun get(index: Int) : T

    fun toList() : List<T>
}

interface KBindableVarArray<T>: KBindableValArray<T> {
    val kbvars : List<KBindableVar<T>>

    operator fun set(index: Int, value: T)
}

abstract class KBindableVarArrayImpl<T>: KBindableVarArray<T> {
    override val kbvals: List<KBindableVal<T>> get() = kbvars

    override fun get(index: Int): T = kbvars[index].value

    override fun set(index: Int, value: T) { kbvars[index].value = value }

    override fun toList(): List<T> = kbvars.map { it.value }
}