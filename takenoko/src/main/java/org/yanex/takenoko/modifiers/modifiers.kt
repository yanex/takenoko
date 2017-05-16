package org.yanex.takenoko

val PUBLIC: KoModifier = KoModifierImpl("public")
val PROTECTED: KoModifier = KoModifierImpl("protected")
val INTERNAL: KoModifier = KoModifierImpl("internal")
val PRIVATE: KoModifier = KoModifierImpl("private")

val FINAL: KoModifier = KoModifierImpl("final")
val SEALED: KoModifier = KoModifierImpl("sealed")
val OPEN: KoModifier = KoModifierImpl("open")
val ABSTRACT: KoModifier = KoModifierImpl("abstract")

val VAL: KoModifier = KoSyntheticModifierImpl("val")
val VAR: KoModifier = KoSyntheticModifierImpl("val")
val VARARG: KoModifier = KoSyntheticModifierImpl("vararg")

val INLINE: KoModifier = KoModifierImpl("inline")
val NOINLINE: KoModifier = KoModifierImpl("noinline")
val CROSSINLINE: KoModifier = KoModifierImpl("crossinline")
val CONST: KoModifier = KoModifierImpl("const")
val REIFIED: KoModifier = KoModifierImpl("reified")
val DATA: KoModifier = KoModifierImpl("data")
val SUSPEND: KoModifier = KoModifierImpl("suspend")

internal val NULLABLE: KoModifier = KoSyntheticModifierImpl("?")
internal val OBJECT: KoModifier = KoSyntheticModifierImpl("object")
internal val INTERFACE: KoModifier = KoSyntheticModifierImpl("interface")
internal val ENUM: KoModifier = KoSyntheticModifierImpl("enum")