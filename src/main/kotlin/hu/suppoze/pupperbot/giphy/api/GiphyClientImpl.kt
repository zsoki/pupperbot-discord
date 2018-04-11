package hu.suppoze.pupperbot.giphy.api

import com.google.gson.Gson
import hu.suppoze.pupperbot.common.http.RestClient
import hu.suppoze.pupperbot.di.kodein
import org.kodein.di.generic.instance

class GiphyClientImpl : GiphyClient {

    private val giphyRandomUrl: String by kodein.instance("giphyRandomUrl")
    private val giphySearchUrl: String by kodein.instance("giphySearchUrl")
    private val client: RestClient by kodein.instance()
    private val gson: Gson by kodein.instance()
    private val demoKey: String = "dc6zaTOxFJmzC" // TODO outsource to external resource

    override fun getRandomGiphyBy(tag: String): String {
        val responseBody = client.get(
            giphyRandomUrl,
            listOf("api_key" to demoKey, "tag" to tag, "rating" to "r")
        )

        val giphyRandomResponse = gson.fromJson(responseBody, GiphyRandomResponse::class.java)
        return giphyRandomResponse.data.url
    }


    override fun searchGiphyBy(phrase: String, limit: Int): GiphySearchResponse {
        val responseBody = client.get(
            giphySearchUrl,
            listOf("api_key" to demoKey, "q" to phrase, "limit" to limit, "rating" to "r")
        )

        return gson.fromJson(responseBody, GiphySearchResponse::class.java)
    }

}