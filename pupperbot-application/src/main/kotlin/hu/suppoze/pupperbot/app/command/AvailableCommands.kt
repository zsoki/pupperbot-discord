package hu.suppoze.pupperbot.app.command

enum class AvailableCommands(val commandString: String) {

    HELP("help") {
        override val description = "You are using this right now, woof!"
        override val example = "`;$commandString`"
    },

    SAY("say") {
        override val description = "Make Pupper say something. Your message will be deleted."
        override val example = "`;$commandString I'm a good booy!`"
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

    CINEMA_CITY("mozi") {
        override val description = "Fetches movies shown in Cinema City Szeged."
        override val example = "`;$commandString`"
    },

    SPAWN_ALERT("spawnalert") {
        override val description = "Subscribe to Black Desert boss spawn notifications."
        override val example = "`;$commandString`"
    },

    EMOTE_REPORT("emotereport") {
        override val description = "Generate guild emote usage statistics on the server."
        override val example = "`;$commandString`"
    };

    abstract val description: String
    abstract val example: String

}