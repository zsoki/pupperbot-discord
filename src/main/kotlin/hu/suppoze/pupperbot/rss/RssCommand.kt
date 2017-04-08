package hu.suppoze.pupperbot.rss

import com.rometools.rome.feed.synd.SyndFeed
import hu.suppoze.pupperbot.common.Command
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder

class RssCommand(val event: MessageReceivedEvent) : Command<SyndFeed> {

    override val onNext: (SyndFeed) -> Unit = {
        val entry = it.entries.first()!!
        val embed = EmbedBuilder()
                .withTitle(entry.title)
                .withAuthorName(it.title)
                .withAuthorUrl(it.link)
                .withDesc(entry.description.value)
                .withUrl(entry.link)
        if (it.image != null) embed.withImage(it.image.url)
        event.message.channel.sendMessage("", embed.build(), false)
    }

    override val onError: (Throwable) -> Unit = Throwable::printStackTrace

    override fun perform() {
        RssService().getFeed().subscribe(onNext, onError)
    }

}
