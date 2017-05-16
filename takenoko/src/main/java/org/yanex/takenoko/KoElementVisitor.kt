package org.yanex.takenoko

interface KoElementVisitor<out T> {
    fun visitElement(e: KoElement): T
    fun visitFile(e: KoFile): T = visitElement(e)

    fun visitDeclaration(e: KoDeclaration): T = visitElement(e)

    fun visitFunction(e: KoFunction): T = visitDeclaration(e)
    fun visitConstructor(e: KoConstructor): T = visitFunction(e)
    fun visitVariable(e: KoVariable): T = visitDeclaration(e)
    fun visitClass(e: KoClass): T = visitDeclaration(e)

    fun visitAnnotation(e: KoAnnotation): T = visitElement(e)
    fun visitTypeParameter(e: KoTypeParameter): T = visitElement(e)
}