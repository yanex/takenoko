package org.yanex.takenoko

interface KoImportList {
    val imports: Map<String, String>
    operator fun get(type: KoType): KoType
}

operator fun KoImportList.get(type: String): KoType {
    return get(KoType.parseType(type))
}