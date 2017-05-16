package org.yanex.takenoko

interface KoClass : KoDeclaration, KoDeclarationOwner, KoAnnotated, KotlinTypeParameterOwner {
    val superClass: SuperClassConstructorCall?
    val interfaces: List<KoType>
    val delegates: List<Pair<KoType, Any?>>

    fun extends(type: KoType, vararg args: Any?)
    fun implements(type: KoType)
    fun delegates(type: KoType, to: Any?)

    fun primaryConstructor(modifiers: KoModifierList = KoModifierList.Empty, block: KoConstructor.() -> Unit = {})
    fun secondaryConstructor(modifiers: KoModifierList = KoModifierList.Empty, block: KoConstructor.() -> Unit = {})

    class SuperClassConstructorCall(val type: KoType, val args: List<Any?>)
}

fun KoClass.extends(type: String, vararg args: Any?) = extends(KoType.parseType(type), *args)

fun KoClass.implements(type: String) = implements(KoType.parseType(type))

fun KoClass.delegates(type: String, to: Any?) = delegates(KoType.parseType(type), to)

inline fun <reified T : Any> KoClass.extends(vararg args: Any?) {
    extends(KoType.parseType(T::class.java), *args)
}

inline fun <reified T : Any> KoClass.implements() {
    implements(KoType.parseType(T::class.java))
}

inline fun <reified Intf : Any, reified Target: Any> KoClass.delegates() {
    delegates(KoType.parseType(Intf::class.java), KoType.parseType(Target::class.java))
}