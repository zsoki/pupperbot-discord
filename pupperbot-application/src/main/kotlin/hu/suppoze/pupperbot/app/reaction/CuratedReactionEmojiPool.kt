package hu.suppoze.pupperbot.app.reaction

class CuratedReactionEmojiPool {
    private val emoji = mutableSetOf(
        "😂", "💩", "❤", "😎", "🤔", "🤷‍", "🐍", "🐎",
        "🦑", "🦄", "👌", "🍕", "🥓", "🍻", "🍆", "💦",
        "🐐", "😺", "🤡", "🐘", "😜", "🐼", "🍷", "👀",
        "🌮", "🥚", "🐓", "🐌", "👨‍❤️‍💋‍👨", "🍑", "🚀", "🏎"
    )

    fun getNext(): String {
        val next = emoji.random()
        emoji.remove(next)
        return next
    }
}