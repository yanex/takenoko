package org.yanex.takenoko

import java.util.*

interface KoModifier : KoModifierList {
    val name: String

    override val modifiers: Set<KoModifier>
        get() = Collections.singleton(this)
}

internal interface KoSyntheticModifier : KoModifier

internal class KoModifierImpl(override val name: String) : KoModifier

internal class KoSyntheticModifierImpl(override val name: String) : KoSyntheticModifier