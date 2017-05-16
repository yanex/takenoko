package org.yanex.takenoko

import org.yanex.takenoko.KoTypeVariance.IN
import org.yanex.takenoko.KoTypeVariance.OUT

interface KoType {
    companion object {
        private val FUNCTION_TYPE_REGEXP = "^(?:(.+)\\.)?\\((.*?)\\)\\s+->\\s+(\\S.*?)$".toRegex()
        private val USER_TYPE_REGEXP = "^([^<>]+)(?:<(.+)>)?$".toRegex()
        private val NAMED_TYPE_REGEXP = "^([^:()\\-_<>]+)\\s*:\\s*(.*)$".toRegex()

        private val MAPPINGS = mapOf(
                String::class.java.canonicalName to "kotlin.String",
                java.lang.Integer::class.java.canonicalName to "kotlin.Int",
                java.lang.Long::class.java.canonicalName to "kotlin.Long",
                java.lang.Short::class.java.canonicalName to "kotlin.Short",
                java.lang.Byte::class.java.canonicalName to "kotlin.Byte",
                java.lang.Character::class.java.canonicalName to "kotlin.Char",
                java.lang.Float::class.java.canonicalName to "kotlin.Float",
                java.lang.Double::class.java.canonicalName to "kotlin.Double",
                java.lang.Boolean::class.java.canonicalName to "kotlin.Boolean",
                Int::class.java.canonicalName to "kotlin.Int",
                Long::class.java.canonicalName to "kotlin.Long",
                Short::class.java.canonicalName to "kotlin.Short",
                Byte::class.java.canonicalName to "kotlin.Byte",
                Char::class.java.canonicalName to "kotlin.Char",
                Float::class.java.canonicalName to "kotlin.Float",
                Double::class.java.canonicalName to "kotlin.Double",
                Boolean::class.java.canonicalName to "kotlin.Boolean",
                CharSequence::class.java.canonicalName to "kotlin.CharSequence",
                List::class.java.canonicalName to "kotlin.collections.List",
                Collection::class.java.canonicalName to "kotlin.collections.Collection"
        )

        val UNIT = KoClassType("kotlin.Unit")
        val NOTHING = KoClassType("kotlin.Nothing")

        val INT = KoClassType("kotlin.Int")
        val LONG = KoClassType("kotlin.Long")
        val SHORT = KoClassType("kotlin.Short")
        val BYTE = KoClassType("kotlin.Byte")
        val CHAR = KoClassType("kotlin.Char")
        val FLOAT = KoClassType("kotlin.Float")
        val DOUBLE = KoClassType("kotlin.Double")
        val BOOLEAN = KoClassType("kotlin.Boolean")

        val STRING = KoClassType("kotlin.String")
        val CHAR_SEQUENCE = KoClassType("kotlin.CharSequence")

        private fun mapBuiltinType(fqName: String): String = MAPPINGS[fqName] ?: fqName

        fun parseType(type: String): KoType {
            val trimmedType = type.trim()

            if (trimmedType.startsWith("in ")) {
                return KoTypeWithVariance(parseType(trimmedType.drop("in ".length)), IN)
            }

            if (trimmedType.startsWith("out ")) {
                return KoTypeWithVariance(parseType(trimmedType.drop("out ".length)), OUT)
            }

            NAMED_TYPE_REGEXP.matchEntire(trimmedType)?.let { named ->
                val (name, type) = named.groupValues.drop(1)
                return KoNamedType(name, parseType(type))
            }

            if (trimmedType.endsWith("?")) {
                return KoNullableType(parseType(trimmedType.dropLast(1)))
            }

            FUNCTION_TYPE_REGEXP.matchEntire(trimmedType)?.let { func ->
                val (rawReceiverType, rawParameterTypes, rawReturnType) = func.groupValues.drop(1)

                val receiverType = if (rawReceiverType.isEmpty()) null else parseType(rawReceiverType)
                val returnType = parseType(rawReturnType)
                val parameterTypes = parseCommaSeparatedTypes(rawParameterTypes.trim())

                return KoFunctionType(receiverType, parameterTypes, returnType)
            }

            USER_TYPE_REGEXP.matchEntire(trimmedType)?.let { typ ->
                val (name, rawTypeArguments) = typ.groupValues.drop(1)
                return KoClassType(name, parseCommaSeparatedTypes(rawTypeArguments.trim()))
            }

            error("Can't parse type $trimmedType")
        }

        fun parseType(javaClass: Class<*>): KoType {
            return if (javaClass.isArray) {
                KoClassType("kotlin.Array", listOf(parseType(javaClass.componentType)))
            } else {
                KoClassType(mapBuiltinType(javaClass.canonicalName), emptyList())
            }
        }

        private fun parseCommaSeparatedTypes(rawParams: String): List<KoType> {
            if (rawParams.isEmpty()) {
                return emptyList()
            }

            val result = mutableListOf<KoType>()

            var depth = 0
            var lastIndex = 0
            for ((index, c) in rawParams.withIndex()) {
                when (c) {
                    '<', '(' -> depth++
                    '>', ')' -> depth--
                    ',' -> if (depth == 0) {
                        result += parseType(rawParams.substring(lastIndex, index))
                        lastIndex = index + 1
                    }
                }
            }

            result += parseType(rawParams.substring(lastIndex, rawParams.length))
            return result
        }
    }
}

class KoTypeWithVariance(val type: KoType, val variance: KoTypeVariance) : KoType

enum class KoTypeVariance(val keyword: String) {
    NONE(""), IN("in"), OUT("out")
}

fun KoElement.KoAnnotatedType(
        type: KoType,
        modifiers: KoModifierList = KoModifierList.Empty,
        block: KoAnnotatedType.() -> Unit = {}
): KoAnnotatedType {
    return KoAnnotatedType(type, this, modifiers, block)
}

class KoAnnotatedType(
        val type: KoType,
        parent: KoElement,
        override val modifiers: KoModifierList = KoModifierList.Empty
) : KoType, KoAnnotated by KoAnnotatedImpl(parent), KoModifierOwner {
    constructor(
            type: KoType,
            parent: KoElement,
            modifiers: KoModifierList = KoModifierList.Empty,
            block: KoAnnotatedType.() -> Unit
    ) : this(type, parent) {
        apply(block)
    }

    init {
        assert(type !is KoAnnotatedType)
    }

    fun copy(type: KoType) = KoAnnotatedType(type).apply {  }
}

class KoNullableType(val type: KoType) : KoType

class KoClassType(
        val name: String,
        val arguments: List<KoType> = emptyList()
) : KoType

fun KoElement.KoFunctionType(receiverType: String?, parameterTypes: List<String>, returnType: String): KoFunctionType {
    return KoFunctionType(receiverType?.let { type(it) }, parameterTypes.map { type(it) }, type(returnType))
}

class KoFunctionType(val receiverType: KoType?, val parameterTypes: List<KoType>, val returnType: KoType) : KoType

class KoNamedType(
        val name: String,
        val type: KoType
) : KoType