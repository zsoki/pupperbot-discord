package hu.suppoze.pupperbot.app.command.gif.api

import com.google.gson.GsonBuilder
import hu.suppoze.pupperbot.app.http.PupperRestClient

private const val GIPHY_RANDOM_URL = "http://api.giphy.com/v1/gifs/random"
private const val GIPHY_SEARCH_URL = "http://api.giphy.com/v1/gifs/search"

private const val demoKey: String = "dc6zaTOxFJmzC"

class GiphyClient(
    private val restClient: PupperRestClient
) : PupperGifClient {

    // TODO: maybe dependency injection would be more ideal?
    private val jsonParser = GsonBuilder().create()

    override fun getRandomGifBy(tag: String): String {
        val responseBody = restClient.get(
            GIPHY_RANDOM_URL,
            listOf("api_key" to demoKey, "tag" to tag, "rating" to "r")
        )

        val giphyRandomResponse = jsonParser.fromJson(responseBody, GiphyRandomResponse::class.java)
        return giphyRandomResponse.data.url
    }


    override fun searchGifBy(phrase: String, limit: Int): GiphySearchResponse {
        val responseBody = restClient.get(
            GIPHY_SEARCH_URL,
            listOf("api_key" to demoKey, "q" to phrase, "limit" to limit, "rating" to "r")
        )

        return jsonParser.fromJson(responseBody, GiphySearchResponse::class.java)
    }
}