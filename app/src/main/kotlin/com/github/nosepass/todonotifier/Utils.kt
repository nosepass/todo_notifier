package com.github.nosepass.todonotifier


/**
 * Convenience function that creates namespaced intent action strings
 * @param action a string like SOME_ACTION
 * @return a namespaced string like com.example.someclass.SOME_ACTION
 */
fun Any.intentString(action: String): String {
    return "${javaClass.getName()}.${action}"
}

