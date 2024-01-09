package com.core.extensions

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

infix fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}

fun List<Any>.indexExist(index: Int): Boolean {
    return index in 0 until size
}

fun Int.toMilliSeconds() = this*1000L
