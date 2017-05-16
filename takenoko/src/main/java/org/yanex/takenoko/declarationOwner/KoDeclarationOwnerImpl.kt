package org.yanex.takenoko

internal class KoDeclarationOwnerImpl(
        parent: KoElement?,
        internal val myImportList: KoImportList? = null
) : KoDeclarationOwner {
    override val declarations = mutableListOf<KoDeclaration>()
    private val _parent = parent

    override val parent: KoElement
        get() = _parent ?: error("This is a top-level element")

    override fun classDeclaration(name: String, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name).apply(block)
    }

    override fun classDeclaration(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, modifiers).apply(block)
    }

    override fun interfaceDeclaration(name: String, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, INTERFACE).apply(block)
    }

    override fun interfaceDeclaration(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, modifiers + INTERFACE).apply(block)
    }

    override fun objectDeclaration(name: String, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, OBJECT).apply(block)
    }

    override fun objectDeclaration(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, modifiers + OBJECT).apply(block)
    }

    override fun enum(name: String, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, ENUM).apply(block)
    }

    override fun enum(name: String, modifiers: KoModifierList, block: KoClass.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoClassImpl(this, name, modifiers + ENUM).apply(block)
    }

    override fun function(name: String, block: KoFunction.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoFunctionImpl(this, name).apply(block)
    }

    override fun property(name: String, type: KoType?, block: KoVariable.() -> Unit) {
        val importedType = type?.let { importList[it] }
        this@KoDeclarationOwnerImpl.declarations += KoVariableImpl(this, name, importedType).apply {
            block()
        }
    }

    override fun property(
            name: String,
            type: KoType?,
            modifiers: KoModifierList,
            block: KoVariable.() -> Unit
    ) {
        val importedType = type?.let { importList[it] }
        this@KoDeclarationOwnerImpl.declarations += KoVariableImpl(this, name, importedType, modifiers).apply {
            block()
        }
    }

    override fun function(name: String, modifiers: KoModifierList, block: KoFunction.() -> Unit) {
        this@KoDeclarationOwnerImpl.declarations += KoFunctionImpl(this, name, modifiers).apply(block)
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitElement(this)
}