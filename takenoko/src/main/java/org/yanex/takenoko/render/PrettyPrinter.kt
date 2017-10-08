package org.yanex.takenoko

import org.yanex.takenoko.KoTypeVariance.NONE
import org.yanex.takenoko.KoElementWithComments.Comment.CommentType.KDOC
import org.yanex.takenoko.KoElementWithComments.Comment.CommentType.NORMAL

class PrettyPrinterConfiguration(val identation: String = "    ")

class PrettyPrinter(val configuration: PrettyPrinterConfiguration) : KoElementVisitor<String> {
    private companion object {
        val LINE_SEPARATOR = System.getProperty("line.separator")!!
    }

    private val String.withIndentation: String
        get() = lineSequence().map { configuration.identation + it }.joinToString(LINE_SEPARATOR)

    private val String.withIndentation2: String
        get() = lineSequence().map { configuration.identation.repeat(2) + it }.joinToString(LINE_SEPARATOR)

    override fun visitElement(e: KoElement) = throw IllegalStateException()

    override fun visitFile(e: KoFile) = buildString {
        e.annotations.render(this)

        val declarations = e.declarations.joinToString(LINE_SEPARATOR + LINE_SEPARATOR) { it.accept(this@PrettyPrinter) }

        if (e.packageName.isNotEmpty()) {
            appendln("package ${e.packageName}")
        }

        if (e.importList.imports.isNotEmpty() && e.packageName.isNotEmpty()) {
            appendln()
        }

        e.importList.imports.forEach { append("import ").appendln(it.value) }

        if (e.annotations.isNotEmpty() || e.packageName.isNotEmpty() || e.importList.imports.isNotEmpty()) {
            appendln()
        }

        append(declarations)
    }

    override fun visitFunction(e: KoFunction) = buildString {
        e.comments.render(this)
        e.annotations.render(this)
        e.modifiers.render(this)

        if (e !is KoConstructor) {
            append("fun ")
        }

        e.typeParameters.render(this, withTrailingSpace = true)

        e.receiverType?.let { receiverType ->
            append(renderType(receiverType)).append('.')
        }

        renderName(e.name)
        e.renderParameterList(this)

        if (e is KoConstructor) {
            e.delegateCall?.let { delegateCall ->
                append(" : ").append(delegateCall.reference).append("(")
                delegateCall.args.joinTo(this) { e.renderExpression(it) }
                append(")")
            }
        } else {
            e.returnType?.let { returnType ->
                append(": ").append(renderType(returnType))
            }
        }

        e.body?.let { body ->
            if (body.expressionBody) {
                append(" = ").append(e.renderExpression(body.text))
            } else {
                appendln(" {")
                appendln(e.renderExpression(body.text).withIndentation)
                append("}")
            }
        }
    }

    override fun visitVariable(e: KoVariable) = buildString {
        e.comments.render(this)
        e.annotations.render(this, singleLine = e.isParameter)
        e.modifiers.render(this)

        if (e.parent is KoFunction) {
            if (e.modifiers[VARARG]) append("vararg ")
            if (e.modifiers[VAR]) append("var ")
            else if (e.modifiers[VAL]) append("val ")
        } else {
            append(if (e.modifiers[VAR]) "var " else "val ")
        }

        e.typeParameters.render(this, withTrailingSpace = true)

        e.receiverType?.let { receiverType ->
            append(renderType(receiverType)).append('.')
        }

        renderName(e.name)

        e.type?.let { type ->
            append(": ").append(renderType(type))
        }

        e.initializer?.let { append(" = ").append(e.renderExpression(it)) }
        e.delegate?.let { append(" by ").append(e.renderExpression(it)) }

        e.getter?.let { getter ->
            appendln().append(configuration.identation)
            getter.annotations.render(this, singleLine = true)
            append("get()")
            if (getter.expressionBody) {
                append(" = ").append(e.renderExpression(getter.text))
            } else {
                appendln(" {").append(e.renderExpression(getter.text).withIndentation2).appendln().append("}")
            }
        }

        e.setter?.let { setter ->
            appendln().append(configuration.identation)
            setter.annotations.render(this, singleLine = false)
            append("set(").append(setter.parameterName).append(")")
            if (setter.expressionBody) {
                append(" = ").append(e.renderExpression(setter.text))
            } else {
                appendln(" {").append(e.renderExpression(setter.text).withIndentation2).appendln().append("}")
            }
        }
    }

    override fun visitClass(e: KoClass) = buildString {
        e.comments.render(this)
        e.annotations.render(this)
        e.modifiers.render(this)

        append(when {
            e.modifiers[OBJECT] -> "object"
            e.modifiers[INTERFACE] -> "interface"
            e.modifiers[ENUM] -> "enum class"
            else -> "class"
        }).append(' ')

        renderName(e.name)
        e.typeParameters.render(this)

        val (primaryConstructors, declarations) = e.declarations.partition { it is KoConstructor && it.isPrimary }

        (primaryConstructors.firstOrNull() as? KoConstructor)?.let { constructor ->
            val hasAnyAnnotationsOrModifiers = constructor.modifiers.modifiers.isNotEmpty()
                    || constructor.annotations.isNotEmpty()

            if (hasAnyAnnotationsOrModifiers) {
                append(' ')
            }

            constructor.modifiers.render(this)
            constructor.annotations.render(this, singleLine = true)

            if (hasAnyAnnotationsOrModifiers) {
                append("constructor")
            }

            if (constructor.parameters.isNotEmpty() || hasAnyAnnotationsOrModifiers) {
                constructor.renderParameterList(this)
            }
        }

        if (e.superClass != null || e.interfaces.isNotEmpty()) {
            append(" : ")
            e.superClass?.let { superClass ->
                append(renderType(superClass.type))
                superClass.args.joinTo(this, prefix = "(", postfix = ")") { e.renderExpression(it) }

                if (e.interfaces.isNotEmpty()) {
                    append(", ")
                }
            }

            e.interfaces.joinTo(this) { renderType(it) }
        }

        if (declarations.isNotEmpty()) {
            declarations.joinTo(this, separator = "\n\n", prefix = " {\n", postfix = "\n}") {
                it.accept(this@PrettyPrinter).withIndentation
            }
        }
    }

    override fun visitTypeParameter(e: KoTypeParameter) = buildString {
        if (e.variance != NONE) {
            append(e.variance.keyword).append(" ")
        }

        append(e.name)

        e.bound?.let { bound ->
            append(" : ").append(renderType(bound))
        }
    }

    override fun visitAnnotation(e: KoAnnotation) = e.render()

    private fun List<KoElementWithComments.Comment>.render(builder: StringBuilder) {
        if (isNotEmpty()) {
            joinTo(builder, separator = "\n", postfix = "\n") { comment ->
                val text = comment.text
                when (comment.type) {
                    NORMAL -> {
                        if (text.lines().count() == 1) {
                            "// " + text
                        } else {
                            "/*\n$text\n*/"
                        }
                    }
                    KDOC -> {
                        "/**\n * " + text.lines().joinToString(separator = "\n * ") + "\n */"
                    }
                }
            }
        }
    }

    private fun StringBuilder.renderName(name: String): StringBuilder {
        assert(name.isNotEmpty()) { "Declaration name should not be empty" }

        if ('$' !in name && name[0].isJavaIdentifierStart() && name.drop(1).all { it.isJavaIdentifierPart() }) {
            append(name)
        } else {
            assert('`' !in name) { "Declaration name should not contain '`' character" }
            append("`$name`")
        }

        return this
    }

    @JvmName("renderTypeParameters")
    private fun List<KoTypeParameter>.render(builder: StringBuilder, withTrailingSpace: Boolean = false) {
        if (isNotEmpty()) {
            joinTo(builder, prefix = "<", postfix = if (withTrailingSpace) "> " else ">") {
                it.accept(this@PrettyPrinter)
            }
        }
    }

    private fun KoFunction.renderParameterList(builder: StringBuilder) {
        parameters.joinTo(builder, prefix = "(", postfix = ")") { it.accept(this@PrettyPrinter) }
    }
}
