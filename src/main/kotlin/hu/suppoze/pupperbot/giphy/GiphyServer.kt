package hu.suppoze.pupperbot.giphy

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.giphy.model.GiphyRandomResponse
import hu.suppoze.pupperbot.giphy.model.GiphySearchResponse
import org.kodein.di.generic.instance

class GiphyServer {

    private val demoKey: String = "dc6zaTOxFJmzC"
    private val giphyRandomUrl: String by kodein.instance("giphyRandomUrl")
    private val giphySearchUrl: String by kodein.instance("giphySearchUrl")

    fun getRandomGiphyBy(tag: String): String {
        val (_, _, result) =
                Fuel.get(giphyRandomUrl, listOf("api_key" to demoKey, "tag" to tag, "rating" to "r"))
                    .timeout(3000)
                    .responseObject<GiphyRandomResponse>()
        val (giphyRandomResponse, fuelError) = result

        if (giphyRandomResponse == null)
            throw fuelError!!.exception

        return giphyRandomResponse.data.url
    }


    fun searchGiphyBy(phrase: String, limit: Int): GiphySearchResponse {
        val (_, _, result) =
                Fuel.get(giphySearchUrl, listOf("api_key" to demoKey, "q" to phrase, "limit" to limit, "rating" to "r"))
                    .timeout(3000)
                    .responseObject<GiphySearchResponse>()
        val (giphySearchResponse, fuelError) = result

        if (giphySearchResponse == null)
            throw fuelError!!.exception

        return giphySearchResponse
    }

}