package org.yanex.takenoko

interface KoAnnotated : KoElement {
    val annotations: List<KoAnnotation>

    fun annotation(annotation: KoAnnotation)
}

fun KoAnnotated.annotation(type: KoType, useSiteTarget: KoAnnotationUseSiteTarget, vararg arguments: Any?) {
    annotation(KoAnnotation(this, importList[type], useSiteTarget, arguments.toList()))
}

inline fun <reified T : Annotation> KoAnnotated.annotation(
        useSiteTarget: KoAnnotationUseSiteTarget,
        vararg arguments: Any?
) {
    annotation(KoType.Companion.parseType(T::class.java), *arguments)
}

fun KoAnnotated.annotation(type: KoType, vararg arguments: Any?) {
    annotation(KoAnnotation(this, importList[type], null, arguments.toList()))
}

fun KoAnnotated.annotation(type: String, useSiteTarget: KoAnnotationUseSiteTarget, vararg arguments: Any?) {
    annotation(KoType.parseType(type), useSiteTarget, *arguments)
}

fun KoAnnotated.annotation(type: String, vararg arguments: Any?) {
    annotation(KoType.parseType(type), *arguments)
}

inline fun <reified T : Annotation> KoAnnotated.annotation(vararg arguments: Any?) {
    annotation(KoType.parseType(T::class.java), *arguments)
}