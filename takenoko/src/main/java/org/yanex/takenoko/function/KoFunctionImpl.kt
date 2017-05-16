package org.yanex.takenoko

internal class KoFunctionImpl(
        override val parent: KoElement,
        override val name: String,
        override val modifiers: KoModifierList = KoModifierList.Empty
) : KoFunction,
        KoAnnotated by KoAnnotatedImpl(parent),
        KoDeclarationOwner by KoDeclarationOwnerImpl(parent),
        KotlinTypeParameterOwner by KotlinTypeParameterOwnerImpl(parent),
        KoElementWithComments by KoElementWithCommentsImpl()
{
    override var receiverType: KoType? = null
    override var returnType: KoType? = null
    override val parameters = mutableListOf<KoVariable>()
    override var body: KoFunction.FunctionBody? = null

    override fun returnType(type: KoType) {
        assert(returnType == null)
        returnType = importList[type]
    }

    override fun receiverType(type: KoType) {
        assert(receiverType == null)
        receiverType = importList[type]
    }

    override fun param(
            name: String,
            type: KoType,
            modifiers: KoModifierList,
            initializer: Any?,
            block: KoVariable.() -> Unit
    ) {
        parameters += KoVariableImpl(this, name, importList[type], modifiers).apply {
            initializer(initializer)
            block()
        }
    }

    override fun body(expressionBody: Boolean, body: KoFunction.FunctionBody.() -> Unit) {
        this.body = KoFunction.FunctionBody(expressionBody = expressionBody).apply(body)
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitFunction(this)
}