package hu.suppoze.pupperbot.app.util

fun String.containsAsciiPrintableIgnoreCase(other: String): Boolean {
    val thisAscii = this.removeNonAsciiCharacters()
    val otherAscii = other.removeNonAsciiCharacters()
    return thisAscii.contains(otherAscii, true)
}

fun String.removeNonAsciiCharacters(): String = this.replace("[^\\x20-\\x7e]", "")

fun String.withPathParams(vararg params: String): String {
    var replaceIndex = 0
    return this.replace(Regex("\\{\\{\\d+}}")) {
        if (replaceIndex <= params.size - 1) params[replaceIndex++] else it.value
    }
}