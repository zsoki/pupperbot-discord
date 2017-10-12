package hu.suppoze.pupperbot.common

enum class AvailableCommands(val commandString: String) {

    HELP("help") {
        override val description = "You are using this right now, woof!"
        override val example = "`;$commandString`"
    },

    GIPHY_RANDOM("random") {
        override val description = "Returns a random gif restricted by tags (R rated!)"
        override val example = "`;$commandString wet pussy cat kittens <add any more tags>`"
    },

    GIPHY_SEARCH("gif") {
        override val description = "Searches for a related gif by a phrase (R rated!)"
        override val example = "`;$commandString just do it!`"
    },

    GIFLAND_NSFW("pron") {
        override val description = "Fetches a random NSFW gif from <http://porn.gifland.us>"
        override val example = "`;$commandString`"
    },

    SAY("say") {
        override val description = "Make Pupper say something. Your message will be deleted."
        override val example = "`;$commandString I'm a good booy!`"
    };

    abstract val description: String
    abstract val example: String

}