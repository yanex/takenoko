package org.yanex.takenoko

internal class KoConstructorImpl(
        parent: KoElement,
        override val isPrimary: Boolean,
        modifiers: KoModifierList
) : KoConstructor, KoFunction by KoFunctionImpl(parent, "constructor", modifiers) {
    override var delegateCall: KoConstructor.DelegateConstructorCall? = null

    override fun delegateCall(reference: String, vararg args: Any?) {
        assert(delegateCall == null)
        delegateCall = KoConstructor.DelegateConstructorCall(reference, args.toList())
    }

    override fun <T> accept(visitor: KoElementVisitor<T>) = visitor.visitConstructor(this)
}