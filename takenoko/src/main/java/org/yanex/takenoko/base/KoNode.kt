package org.yanex.takenoko

@DslMarker
annotation class KoNodeMarker

@KoNodeMarker
interface KoNode {
    val parent: KoElement
}

internal class KoNodeWithParent(parent: KoElement?): KoNode {
    private val _parent = parent

    override val parent: KoElement
        get() = _parent ?: error("This is a top-level element")
}