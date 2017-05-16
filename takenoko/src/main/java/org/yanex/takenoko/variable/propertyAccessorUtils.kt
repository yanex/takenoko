package org.yanex.takenoko

fun KoVariable.getter(text: String) {
    getter(KoModifierList.Empty, false) { append(text.trimIndent()) }
}

fun KoVariable.getter(modifiers: KoModifierList, text: String) {
    getter(modifiers, false) { append(text.trimIndent()) }
}

fun KoVariable.setter(parameterName: String = "v", text: String) {
    setter(KoModifierList.Empty, false, parameterName) { append(text.trimIndent()) }
}

fun KoVariable.setter(modifiers: KoModifierList, parameterName: String = "v", text: String) {
    setter(modifiers, false, parameterName) { append(text.trimIndent()) }
}

fun KoVariable.getterExpression(text: String) {
    getter(KoModifierList.Empty, true) { append(text.trimIndent()) }
}

fun KoVariable.getterExpression(modifiers: KoModifierList, text: String) {
    getter(modifiers, true) { append(text.trimIndent()) }
}

fun KoVariable.setterExpression(parameterName: String = "v", text: String) {
    setter(KoModifierList.Empty, true, parameterName) { append(text.trimIndent()) }
}

fun KoVariable.setterExpression(modifiers: KoModifierList, parameterName: String = "v", text: String) {
    setter(modifiers, true, parameterName) { append(text.trimIndent()) }
}

fun KoVariable.getter(block: KoVariable.Getter.() -> Unit) {
    getter(KoModifierList.Empty, false, block)
}

fun KoVariable.getter(modifiers: KoModifierList, block: KoVariable.Getter.() -> Unit) {
    getter(modifiers, false, block)
}

fun KoVariable.setter(parameterName: String = "v", block: KoVariable.Setter.() -> Unit) {
    setter(KoModifierList.Empty, false, parameterName, block)
}

fun KoVariable.setter(modifiers: KoModifierList, parameterName: String = "v", block: KoVariable.Setter.() -> Unit) {
    setter(modifiers, false, parameterName, block)
}

fun KoVariable.getterExpression(block: KoVariable.Getter.() -> Unit) {
    getter(KoModifierList.Empty, true, block)
}

fun KoVariable.getterExpression(modifiers: KoModifierList, block: KoVariable.Getter.() -> Unit) {
    getter(modifiers, true, block)
}

fun KoVariable.setterExpression(parameterName: String = "v", block: KoVariable.Setter.() -> Unit) {
    setter(KoModifierList.Empty, true, parameterName, block)
}

fun KoVariable.setterExpression(modifiers: KoModifierList, parameterName: String = "v", block: KoVariable.Setter.() -> Unit) {
    setter(modifiers, true, parameterName, block)
}