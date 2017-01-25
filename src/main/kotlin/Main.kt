import sx.blah.discord.handle.obj.Permissions
import java.util.*

fun main(args: Array<String>) {

    if (args.count() < 1) throw RuntimeException("Need at least 1 argument!")

    val client = Example().createClient(args[0], true) // Gets the client object (from the first example)
    val dispatcher = client!!.dispatcher // Gets the EventDispatcher instance for this client instance
    dispatcher.registerListener(AnnotationListener()) // Registers the @EventSubscriber example class from above

    val permissions = Permissions.generatePermissionsNumber(EnumSet.copyOf(mutableListOf(Permissions.SEND_MESSAGES, Permissions.READ_MESSAGES)))

    print("Invite link: https://discordapp.com/api/oauth2/authorize?client_id=${client.applicationClientID}&scope=bot&permissions=$permissions\n")

}