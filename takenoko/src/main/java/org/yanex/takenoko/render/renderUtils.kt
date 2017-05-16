package org.yanex.takenoko

@JvmName("renderAnnotation")
internal fun KoAnnotation.render() = buildString {
    append("@")

    if (useSiteTarget != null) {
        append(useSiteTarget.keyword).append(":")
    }

    this.append(renderType(type))

    if (arguments.isNotEmpty()) {
        append(arguments.joinToString(prefix = "(", postfix = ")") { renderExpression(it) })
    }
}

private val TYPE_REGEX = "#\\{(.+?)\\}".toRegex()

internal fun KoElement.renderExpression(expression: Any?): String {
    var importList: KoImportList? = null

    return TYPE_REGEX.replace(expression.toString()) { match ->
        val importList = (importList ?: run {
            val lazyImportList = this@renderExpression.importList
            importList = lazyImportList
            lazyImportList
        })

        renderType(importList[match.groupValues[1]])
    }
}

internal fun KoModifierList.render(builder: StringBuilder) {
    val nonSyntheticModifiers = modifiers.filter { it !is KoSyntheticModifier }
    if (nonSyntheticModifiers.isNotEmpty()) {
        nonSyntheticModifiers.joinTo(builder, " ") { it.name }
        builder.append(' ')
    }
}

internal fun List<KoAnnotation>.render(builder: StringBuilder, singleLine: Boolean = false) {
    if (singleLine) {
        if (isNotEmpty()) {
            joinTo(builder, " ", postfix = " ") { it.render() }
        }
    } else {
        this.forEach { builder.appendln(it.render()) }
    }
}

internal fun renderType(type: KoType): String = when (type) {
    is KoTypeWithVariance -> {
        if (type.variance == KoTypeVariance.NONE)
            renderType(type.type)
        else
            type.variance.keyword + " " + renderType(type.type)
    }
    is KoNamedType -> type.name + ": " + renderType(type.type)
    is KoNullableType -> {
        if (type.type is KoClassType)
            renderType(type.type) + "?"
        else
            "(" + renderType(type.type) + ")?"
    }
    is KoAnnotatedType -> {
        val builder = StringBuilder()
        type.modifiers.render(builder)
        type.annotations.render(builder, singleLine = true)
        builder.append(renderType(type.type))
        builder.toString()
    }
    is KoClassType -> {
        if (type.arguments.isEmpty()) {
            type.name
        } else {
            val builder = StringBuilder(type.name)
            type.arguments.joinTo(builder, prefix = "<", postfix = ">") { renderType(it) }
            builder.toString()
        }
    }
    is KoFunctionType -> {
        val builder = StringBuilder()

        type.receiverType?.let { recevierType ->
            builder.append(renderType(recevierType)).append('.')
        }

        type.parameterTypes.joinTo(builder, prefix = "(", postfix = ")") { renderType(it) }

        builder.append(" -> ").append(renderType(type.returnType))

        builder.toString()
    }
    else -> error("Unexpected type ${type.javaClass.name}")
}