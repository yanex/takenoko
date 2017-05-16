package org.yanex.takenoko

interface KoFunction : KoDeclaration, KoAnnotated, KotlinTypeParameterOwner {
    val receiverType: KoType?
    val returnType: KoType?
    val parameters: List<KoVariable>
    val body: FunctionBody?

    fun returnType(type: KoType)

    fun receiverType(type: KoType)

    fun param(
            name: String,
            type: KoType,
            modifiers: KoModifierList,
            initializer: Any? = null,
            block: KoVariable.() -> Unit = {})

    fun body(expressionBody: Boolean = false, body: FunctionBody.() -> Unit)

    class FunctionBody(val expressionBody: Boolean): KoBuffer()
}

fun KoFunction.body(expressionBody: Boolean = false, body: String) {
    body(expressionBody) { append(body.trimIndent()) }
}

fun KoFunction.body(body: String) = body(expressionBody = false, body = body)

fun KoFunction.expressionBody(body: String) = body(expressionBody = true, body = body)
fun KoFunction.expressionBody(body: KoFunction.FunctionBody.() -> Unit) = body(expressionBody = true, body = body)

fun KoFunction.returnType(type: String) = returnType(KoType.parseType(type))

fun KoFunction.receiverType(type: String) = receiverType(KoType.parseType(type))

fun KoFunction.param(name: String, type: KoType, initializer: Any? = null, block: KoVariable.() -> Unit = {}) {
    param(name, type, KoModifierList.Empty, initializer, block)
}

fun KoFunction.param(
        name: String,
        type: String,
        defaultValue: Any? = null,
        block: KoVariable.() -> Unit = {}
) {
    param(name, KoType.parseType(type), defaultValue, block)
}

fun KoFunction.param(
        name: String,
        modifiers: KoModifierList,
        type: String,
        defaultValue: Any? = null,
        block: KoVariable.() -> Unit = {}
) {
    param(name, KoType.parseType(type), modifiers, defaultValue, block)
}

inline fun <reified T : Any> KoFunction.receiverType() {
    receiverType(KoType.parseType(T::class.java))
}

inline fun <reified T : Any> KoFunction.returnType() {
    returnType(KoType.Companion.parseType(T::class.java))
}

inline fun <reified T : Any> KoFunction.param(
        name: String,
        initializer: Any? = null,
        noinline block: KoVariable.() -> Unit = {}
) {
    param(name, KoType.parseType(T::class.java), initializer, block)
}

inline fun <reified T : Any> KoFunction.param(
        name: String,
        modifiers: KoModifierList,
        initializer: Any? = null,
        noinline block: KoVariable.() -> Unit = {}
) {
    param(name, KoType.parseType(T::class.java), modifiers, initializer, block)
}