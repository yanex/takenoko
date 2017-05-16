package org.yanex.takenoko

import org.yanex.takenoko.KoElementWithComments.Comment

class KoElementWithCommentsImpl : KoElementWithComments {
    override var comments = mutableListOf<Comment>()

    override fun comment(type: Comment.CommentType, block: KoBuffer.() -> Unit) {
        comments.add(Comment(type, KoBuffer().apply(block).text))
    }
}