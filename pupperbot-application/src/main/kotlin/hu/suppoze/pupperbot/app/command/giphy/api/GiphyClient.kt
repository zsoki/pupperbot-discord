package hu.suppoze.pupperbot.app.command.giphy.api

interface GiphyClient {

    fun getRandomGiphyBy(tag: String): String

    fun searchGiphyBy(phrase: String, limit: Int): GiphySearchResponse

}
