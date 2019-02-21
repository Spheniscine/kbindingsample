package com.github.spheniscine.kbinding

interface KBindableValArray<T>: List<T> {
    val kbvals : List<KBindableVal<T>>

    override fun get(index: Int) : T = kbvals[index].value

    private fun toList() : List<T> = kbvals.map { it.value }

    fun <R> merge(result: (List<T>) -> R): KBindableVal<R> = kbvals.merge { result(toList()) }

    override val size get() = kbvals.size
    override fun isEmpty() = kbvals.isEmpty()
    override fun contains(element: T) = toList().contains(element)
    override fun iterator(): Iterator<T> = toList().iterator()
    override fun containsAll(elements: Collection<T>) = toList().containsAll(elements)

    override fun indexOf(element: T): Int = toList().indexOf(element)
    override fun lastIndexOf(element: T): Int = toList().lastIndexOf(element)
    override fun listIterator(): ListIterator<T> = toList().listIterator()
    override fun listIterator(index: Int): ListIterator<T> = toList().listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<T> = toList().subList(fromIndex, toIndex)

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

        operator fun <T> invoke(size: Int, lateInitMarker: LATEINIT) =
            invoke(List(size) { KBindableVar<T>(LATEINIT) })

        fun <T> of(vararg elements: T) = invoke(elements.map{ KBindableVar(it) })
    }
}

