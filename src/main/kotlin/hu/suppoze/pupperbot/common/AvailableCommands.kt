package hu.suppoze.pupperbot.common

enum class AvailableCommands(val commandString: String) {

    HELP("help") {
        override val description = "You are using this right now, woof!"
        override val usage = "```;help```"
    },

    GIPHY("giphy") {
        override val description = "Returns an random gif from giphy restricted by tags (R rated!)"
        override val usage = "```;giphy tag1 tag2 (...) tagn```"
    },

    SAY("say") {
        override val description = "Make Pupper say something. Your message will be deleted."
        override val usage = "```;say I'm a good booy!```"
    },

    RSSSUB("rsssub") {
        override val description = "Sub to an RSS feed by URL on this channel (UNDER DEVELOPMENT)"
        override val usage = "```;rsssub http://example.com/feedurl```"
    },

    RSSUNSUB("rssunsub") {
        override val description = "Unsubscribe from an RSS feed on this channel (UNDER DEVELOPMENT)"
        override val usage = "```;rssunsub http://example.com/feedurl```"
    };

    abstract val description : String
    abstract val usage : String

}