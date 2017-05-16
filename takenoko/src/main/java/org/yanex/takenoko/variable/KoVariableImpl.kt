package org.yanex.takenoko

class KoVariableImpl(
        override val parent: KoElement,
        override val name: String,
        override val type: KoType?,
        override val modifiers: KoModifierList = KoModifierList.Empty
) : KoVariable,
        KoAnnotated by KoAnnotatedImpl(parent),
        KotlinTypeParameterOwner by KotlinTypeParameterOwnerImpl(parent),
        KoElementWithComments by KoElementWithCommentsImpl()
{
    override var receiverType: KoType? = null
    override var initializer: Any? = null
    override var delegate: Any? = null
    override var getter: KoVariable.Getter? = null
    override var setter: KoVariable.Setter? = null

    override fun receiverType(type: KoType) {
        assert(receiverType == null)
        receiverType = importList[type]
    }

    override fun initializer(value: Any?) {
        assert(initializer == null && delegate == null)
        initializer = value
    }

    override fun delegate(value: Any?) {
        assert(delegate == null && initializer == null)
        delegate = value
    }

    override fun getter(modifiers: KoModifierList, expressionBody: Boolean, block: KoVariable.Getter.() -> Unit) {
        assert(getter == null)
        getter = KoVariable.Getter(this, modifiers, expressionBody).apply(block)
    }

    override fun setter(
            modifiers: KoModifierList,
            expressionBody: Boolean,
            parameterName: String,
            block: KoVariable.Setter.() -> Unit
    ) {
        assert(setter == null)
        setter = KoVariable.Setter(this, modifiers, expressionBody, parameterName).apply(block)
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitVariable(this)
}