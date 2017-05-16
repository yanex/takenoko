package org.yanex.takenoko

interface KoElement : KoNode {
    fun <T> accept(visitor: KoElementVisitor<T>): T
}