package org.yanex.takenoko

open class KoBuffer {
    private val builder = StringBuilder()

    val text: String
        get() = builder.toString()

    fun append(text: String) = builder.append(text)
    fun appendln(text: String) = builder.appendln(text)
}