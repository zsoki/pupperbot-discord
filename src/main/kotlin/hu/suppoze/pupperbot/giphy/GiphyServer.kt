package hu.suppoze.pupperbot.giphy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.giphy.model.GiphyRandomResponse
import hu.suppoze.pupperbot.giphy.model.GiphySearchResponse
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.experimental.rx2.rxObservable
import java.util.concurrent.TimeUnit

class GiphyServer {

    private val demoKey: String = "dc6zaTOxFJmzC"
    private val giphyRandomUrl: String by kodein.instance("giphyRandomUrl")
    private val giphySearchUrl: String by kodein.instance("giphySearchUrl")

    fun getRandomGiphyBy(tag: String): Single<String> {
        return giphyRandomUrl
                .httpGet(listOf("api_key" to demoKey, "tag" to tag, "rating" to "r")) // The API key is public demo key, don't panic
                .rx_object(GiphyRandomResponse.Deserializer())
                .timeout(3, TimeUnit.SECONDS)
                .retry(1)
                .map { it.get().data.url }
    }

    fun searchGiphyBy(phrase: String, limit: Int): Single<GiphySearchResponse> {
        return giphySearchUrl
                .httpGet(listOf("api_key" to demoKey, "q" to phrase, "limit" to limit, "rating" to "r"))
                .rx_object(GiphySearchResponse.Deserializer())
                .timeout(3, TimeUnit.SECONDS)
                .retry(1)
                .map { it.get() }
    }

}