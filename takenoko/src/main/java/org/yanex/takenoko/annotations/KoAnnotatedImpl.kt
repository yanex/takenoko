package org.yanex.takenoko

internal class KoAnnotatedImpl(parent: KoElement?) : KoAnnotated {
    override val annotations = mutableListOf<KoAnnotation>()
    private val _parent = parent

    override val parent: KoElement
        get() = _parent ?: error("This is a top-level element")

    override fun annotation(annotation: KoAnnotation) {
        annotations += annotation
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitElement(this)
}