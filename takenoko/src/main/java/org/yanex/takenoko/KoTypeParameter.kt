package org.yanex.takenoko

import org.yanex.takenoko.KoTypeVariance.NONE

interface KoTypeParameter : KoElement {
    val name: String
    val bound: KoType?
    val variance: KoTypeVariance
}

interface KotlinTypeParameterOwner : KoElement {
    val typeParameters: List<KoTypeParameter>

    fun typeParam(name: String, type: KoType? = null, variance: KoTypeVariance = NONE)
}

fun KotlinTypeParameterOwner.typeParam(name: String, type: String, variance: KoTypeVariance = NONE) {
    typeParam(name, KoType.parseType(type))
}

inline fun <reified T : Any> KotlinTypeParameterOwner.typeParam(name: String, variance: KoTypeVariance = NONE) {
    typeParam(name, KoType.Companion.parseType(T::class.java))
}

internal class KoTypeParameterImpl(
        override val parent: KoElement,
        override val name: String,
        override val bound: KoType?,
        override val variance: KoTypeVariance
) : KoTypeParameter {
    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitTypeParameter(this)
}

internal class KotlinTypeParameterOwnerImpl(
        parent: KoElement?
) : KotlinTypeParameterOwner, KoNode by KoNodeWithParent(parent) {
    override val typeParameters = mutableListOf<KoTypeParameter>()

    override fun typeParam(name: String, type: KoType?, variance: KoTypeVariance) {
        typeParameters += KoTypeParameterImpl(this, name, type?.let { importList[it] }, variance)
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitElement(this)
}