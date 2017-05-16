package org.yanex.takenoko

interface KoModifierList {
    val modifiers: Set<KoModifier>

    operator fun get(modifier: KoModifier): Boolean = modifier in this.modifiers

    object Empty : KoModifierList {
        override val modifiers = emptySet<KoModifier>()
    }
}

fun KoModifierList.isEmpty(): Boolean = modifiers.isEmpty()

fun KoModifierList.isNotEmpty(): Boolean = !isEmpty()

operator fun KoModifierList.plus(other: KoModifierList): KoModifierList {
    return KoModifierListImpl(modifiers + other.modifiers)
}

internal class KoModifierListImpl(override val modifiers: Set<KoModifier>) : KoModifierList