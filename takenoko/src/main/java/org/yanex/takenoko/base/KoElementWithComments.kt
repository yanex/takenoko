package org.yanex.takenoko

import org.yanex.takenoko.KoElementWithComments.Comment.CommentType.KDOC
import org.yanex.takenoko.KoElementWithComments.Comment.CommentType.NORMAL

interface KoElementWithComments {
    val comments: List<Comment>
    fun comment(type: Comment.CommentType = NORMAL, block: KoBuffer.() -> Unit)

    class Comment(val type: CommentType, val text: String) {
        enum class CommentType {
            NORMAL, KDOC
        }
    }
}

fun KoElementWithComments.kdoc(block: KoBuffer.() -> Unit) {
    comment(KDOC, block)
}

fun KoElementWithComments.comment(text: String) {
    comment { append(text.trimIndent()) }
}

fun KoElementWithComments.kdoc(text: String) {
    comment(KDOC) { append(text.trimIndent()) }
}