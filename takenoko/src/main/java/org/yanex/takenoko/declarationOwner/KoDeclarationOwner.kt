package org.yanex.takenoko

interface KoDeclarationOwner : KoElement {
    val declarations: List<KoDeclaration>

    fun property(name: String, type: KoType? = null, block: KoVariable.() -> Unit = {})

    fun property(name: String, type: KoType? = null, modifiers: KoModifierList, block: KoVariable.() -> Unit = {})

    fun classDeclaration(name: String, block: KoClass.() -> Unit = {})

    fun classDeclaration(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit = {})

    fun interfaceDeclaration(name: String, block: KoClass.() -> Unit = {})

    fun interfaceDeclaration(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit = {})

    fun objectDeclaration(name: String, block: KoClass.() -> Unit = {})

    fun objectDeclaration(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit = {})

    fun enum(name: String, block: KoClass.() -> Unit = {})

    fun enum(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit = {})

    fun function(name: String, block: KoFunction.() -> Unit = {})

    fun function(name: String, modifiers: KoModifierList, block: KoFunction.() -> Unit = {})
}

fun KoDeclarationOwner.property(name: String, type: String, modifiers: KoModifierList, block: KoVariable.() -> Unit = {}) {
    property(name, KoType.parseType(type), modifiers, block)
}

fun KoDeclarationOwner.property(name: String, type: String, block: KoVariable.() -> Unit = {}) {
    property(name, KoType.parseType(type), block)
}

inline fun <reified T : Any> KoDeclarationOwner.property(name: String, noinline block: KoVariable.() -> Unit = {}) {
    property(name, KoType.parseType(T::class.java), block)
}

inline fun <reified T : Any> KoDeclarationOwner.property(
        name: String,
        modifiers: KoModifierList,
        noinline block: KoVariable.() -> Unit = {}
) {
    property(name, KoType.parseType(T::class.java), modifiers, block)
}