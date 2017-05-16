package org.yanex.takenoko

interface KoFile : KoAnnotated, KoDeclarationOwner {
    val packageName: String
    val importList: KoImportList

    fun import(type: KoType): KoType = importList[type]
    fun import(type: String): KoType = importList[type]
}

fun KoElement.type(type: String): KoType = importList[type]

fun kotlinFile(packageName: String = "", block: KoFile.() -> Unit = {}): KoFile {
    return KoFileImpl(packageName).apply(block)
}