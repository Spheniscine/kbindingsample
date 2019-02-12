package com.github.spheniscine.kbinding

interface KBindableValArray<T>: Collection<T> {
    val kbvals : List<KBindableVal<T>>

    operator fun get(index: Int) : T = kbvals[index].value

    fun toList() : List<T> = kbvals.map { it.value }

    override val size get() = kbvals.size
    override fun isEmpty() = kbvals.isEmpty()
    override fun contains(element: T) = toList().contains(element)
    override fun iterator(): Iterator<T> = toList().iterator()
    override fun containsAll(elements: Collection<T>) = toList().containsAll(elements)
}

interface KBindableVarArray<T>: KBindableValArray<T> {
    val kbvars : List<KBindableVar<T>>
    override val kbvals: List<KBindableVal<T>> get() = kbvars

    operator fun set(index: Int, value: T) { kbvars[index].value = value }

    companion object {
        operator fun <T> invoke(kbvars: List<KBindableVar<T>>) =
            object : KBindableVarArray<T> {
                override val kbvars = kbvars
            }

        inline operator fun <T> invoke(size: Int, init: (Int) -> T) =
            invoke(List(size) { KBindableVar(init(it)) })
    }
}

