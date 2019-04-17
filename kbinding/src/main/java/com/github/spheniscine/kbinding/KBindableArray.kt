package com.github.spheniscine.kbinding

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

interface KBindableValArray<T>: List<T> {
    val kbvals : List<KBindableVal<T>>
    fun getKbval(index: Int) = kbvals[index]

    val kvals : List<KProperty0<T>> get() = kbvals.map { it.kval }
    fun getKval(index: Int) = kbvals[index].kval

    override fun get(index: Int) : T = kbvals[index].value

    fun <R> merge(result: (List<T>) -> R): KBindableVal<R> = kbvals.merge { result(this) }

    override val size get() = kbvals.size

    companion object {
        operator fun <T> invoke(kbvals: List<KBindableVal<T>>): KBindableValArray<T> =
            object : AbstractKBindableValArray<T>() {
                override val kbvals = kbvals
            }
    }
}

abstract class AbstractKBindableValArray<T>: KBindableValArray<T>, AbstractList<T>()

interface KBindableVarArray<T>: KBindableValArray<T> {
    val kbvars : List<KBindableVar<T>>
    fun getKbvar(index: Int) = kbvars[index]

    val kvars : List<KMutableProperty0<T>> get() = kbvars.map { it.kvar }
    fun getKvar(index: Int) = kbvars[index].kvar

    override val kbvals: List<KBindableVal<T>> get() = kbvars

    operator fun set(index: Int, value: T) { kbvars[index].value = value }

    companion object {
        operator fun <T> invoke(kbvars: List<KBindableVar<T>>): KBindableVarArray<T> =
            object : AbstractKBindableVarArray<T>() {
                override val kbvars = kbvars
            }

        inline operator fun <T> invoke(size: Int, init: (Int) -> T) =
            invoke(List(size) { KBindableVar(init(it)) })

        operator fun <T> invoke(size: Int, lateInitMarker: LATEINIT) =
            invoke(List(size) { KBindableVar<T>(LATEINIT) })

        fun <T> of(vararg elements: T) = invoke(elements.map{ KBindableVar(it) })
    }
}

abstract class AbstractKBindableVarArray<T>: KBindableVarArray<T>, AbstractKBindableValArray<T>()

