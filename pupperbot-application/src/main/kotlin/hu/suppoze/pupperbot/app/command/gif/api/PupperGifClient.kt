package hu.suppoze.pupperbot.app.command.gif.api

interface PupperGifClient {

    fun getRandomGifBy(tag: String): String

    fun searchGifBy(phrase: String, limit: Int): GiphySearchResponse

}
