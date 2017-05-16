package org.yanex.takenoko

interface KoConstructor : KoFunction {
    val isPrimary: Boolean

    val delegateCall: DelegateConstructorCall?
    fun delegateCall(reference: String, vararg args: Any?)

    class DelegateConstructorCall(val reference: String, val args: List<Any?>)
}