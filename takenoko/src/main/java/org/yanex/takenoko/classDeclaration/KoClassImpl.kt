package org.yanex.takenoko

internal class KoClassImpl(
        override val parent: KoElement,
        override val name: String,
        override val modifiers: KoModifierList = KoModifierList.Empty
) : KoClass,
        KoDeclarationOwner by KoDeclarationOwnerImpl(parent),
        KoAnnotated by KoAnnotatedImpl(parent),
        KotlinTypeParameterOwner by KotlinTypeParameterOwnerImpl(parent),
        KoElementWithComments by KoElementWithCommentsImpl()
{
    override var superClass: KoClass.SuperClassConstructorCall? = null
    override val interfaces = mutableListOf<KoType>()
    override val delegates = mutableListOf<Pair<KoType, Any?>>()

    override fun extends(type: KoType, vararg args: Any?) {
        assert(superClass == null)
        superClass = KoClass.SuperClassConstructorCall(importList[type], args.toList())
    }

    override fun primaryConstructor(modifiers: KoModifierList, block: KoConstructor.() -> Unit) {
        (declarations as MutableList<KoDeclaration>) += KoConstructorImpl(this, true, modifiers).apply(block)
    }

    override fun secondaryConstructor(modifiers: KoModifierList, block: KoConstructor.() -> Unit) {
        (declarations as MutableList<KoDeclaration>) += KoConstructorImpl(this, false, modifiers).apply(block)
    }

    override fun implements(type: KoType) {
        interfaces += importList[type]
    }

    override fun delegates(type: KoType, to: Any?) {
        delegates += Pair(importList[type], to)
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitClass(this)
}