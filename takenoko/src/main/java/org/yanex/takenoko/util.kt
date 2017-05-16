package org.yanex.takenoko

val KoElement.importList: KoImportList
    get() = when (this) {
        is KoFile -> importList
        is KoDeclarationOwnerImpl -> myImportList ?: parent.importList
        else -> parent.importList
    }

fun stringLiteral(s: String): String {
    val builder = StringBuilder("\"")
    for (c in s) {
        builder.append(escapeCharacter(c))
    }
    return builder.append("\"").toString()
}

fun charLiteral(c: Char): String = "'" + escapeCharacter(c) + "'"

private fun escapeCharacter(c: Char): String = when (c) {
    '\n' -> "\\n"
    '\r' -> "\\r"
    '\t' -> "\\t"
    else -> "$c"
}