package hu.suppoze.pupperbot.app.giphy.api

interface GiphyClient {

    fun getRandomGiphyBy(tag: String): String

    fun searchGiphyBy(phrase: String, limit: Int): GiphySearchResponse

}
