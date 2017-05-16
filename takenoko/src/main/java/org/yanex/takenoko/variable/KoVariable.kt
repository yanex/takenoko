package org.yanex.takenoko

interface KoVariable : KoDeclaration, KoAnnotated, KotlinTypeParameterOwner {
    val receiverType: KoType?
    val type: KoType?

    val initializer: Any?
    val delegate: Any?

    val getter: Getter?
    val setter: Setter?

    fun receiverType(type: KoType)

    fun initializer(value: Any?)
    fun delegate(value: Any?)

    fun getter(
            modifiers: KoModifierList,
            expressionBody: Boolean = false,
            block: Getter.() -> Unit)

    fun setter(
            modifiers: KoModifierList,
            expressionBody: Boolean = false,
            parameterName: String = "v",
            block: Setter.() -> Unit)

    interface KoPropertyAccessor : KoAnnotated, KoModifierOwner

    class Getter(
            parent: KoElement,
            override val modifiers: KoModifierList,
            val expressionBody: Boolean
    ) : KoBuffer(), KoPropertyAccessor, KoAnnotated by KoAnnotatedImpl(parent)

    class Setter(
            parent: KoElement,
            override val modifiers: KoModifierList,
            val expressionBody: Boolean,
            val parameterName: String
    ) : KoBuffer(), KoPropertyAccessor, KoAnnotated by KoAnnotatedImpl(parent)
}

fun KoVariable.receiverType(type: String) = receiverType(KoType.parseType(type))

inline fun <reified T : Any> KoVariable.receiverType() {
    receiverType(KoType.parseType(T::class.java))
}

val KoVariable.isProperty: Boolean
    get() = parent !is KoFunction

val KoVariable.isParameter: Boolean
    get() = parent is KoFunction