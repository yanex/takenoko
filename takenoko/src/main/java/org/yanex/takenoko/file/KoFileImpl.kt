package org.yanex.takenoko

internal class KoFileImpl(
        override val packageName: String,
        override val importList: KoImportList = KoImportListImpl()
) : KoFile, KoAnnotated by KoAnnotatedImpl(null), KoDeclarationOwner by KoDeclarationOwnerImpl(null, importList) {
    override val parent: KoElement
        get() = error("KotlinFile is a top-level element")

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitFile(this)
}