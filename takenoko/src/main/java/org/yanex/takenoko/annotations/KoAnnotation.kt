package org.yanex.takenoko

class KoAnnotation(
        override val parent: KoElement,
        val type: KoType,
        val useSiteTarget: KoAnnotationUseSiteTarget?,
        val arguments: List<Any?>
): KoElement {
    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitAnnotation(this)
}

enum class KoAnnotationUseSiteTarget(val keyword: String) {
    FILE("file"),
    PROPERTY("property"),
    PARAMETER("param"),
    FIELD("field"),
    GETTER("get"),
    SETTER("set"),
    RECEIVER("receiver"),
    SET_PARAMETER("setparam")
}