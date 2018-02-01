package dv.serg.lib.dao

interface Key

interface OrdinalKey : Key {
    val id: Long
}

interface DoubleKey : Key {
    val fKey: Long
    val sKey: Long
}

interface TripleKey : DoubleKey {
    val thKey: Long?
}

interface ArticleKey : DoubleKey