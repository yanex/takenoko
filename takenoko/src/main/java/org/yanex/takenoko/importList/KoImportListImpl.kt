package org.yanex.takenoko

class KoImportListImpl : KoImportList {
    private companion object {
        private val DEFAULT_IMPORTS = listOf("kotlin.")
        private val BUILTIN_TYPES = setOf("Int", "Long", "Short", "Char", "Byte",
                "Float", "Double", "Boolean", "String", "CharSequence", "Array")
    }

    override val imports = mutableMapOf<String, String>()

    private fun import(name: String): String {
        val simpleName = name.split('.')
                .takeLastWhile { it.firstOrNull()?.isUpperCase() ?: false }
                .joinToString(".")

        if (simpleName == name) {
            return name
        }

        if (isInDefaultImports(name) && simpleName !in imports) {
            return simpleName
        } else if (simpleName in BUILTIN_TYPES) {
            return name
        }

        if (simpleName[0].isLowerCase() || !simpleName[0].isJavaIdentifierStart()) {
            return name
        }

        if (simpleName in imports) {
            return if (imports[simpleName] == name) simpleName else name
        }

        imports[simpleName] = name
        return simpleName
    }

    private fun isInDefaultImports(fqName: String): Boolean {
        for (import in DEFAULT_IMPORTS) {
            if (!fqName.startsWith(import)) continue
            return '.' !in fqName.substring(import.length)
        }

        return false
    }

    override operator fun get(type: KoType): KoType = when (type) {
        is KoClassType -> KoClassType(import(type.name), type.arguments.map { get(it) })
        is KoFunctionType -> KoFunctionType(
                type.receiverType?.let { get(it) },
                type.parameterTypes.map { get(it) },
                get(type.returnType))
        is KoTypeWithVariance -> KoTypeWithVariance(get(type.type), type.variance)
        is KoNamedType -> KoNamedType(type.name, get(type.type))
        is KoNullableType -> KoNullableType(get(type.type))
        is KoAnnotatedType -> type.copy(type = get(type.type))
        else -> error("Type ${type.javaClass.name} is not supported by ImportList")
    }
}
