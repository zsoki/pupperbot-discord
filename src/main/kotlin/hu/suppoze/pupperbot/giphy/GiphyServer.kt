package hu.suppoze.pupperbot.giphy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.di.kodein
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class GiphyServer {

    private val giphyUrl: String by kodein.instance("giphyUrl")

    fun requestRandomGiphyUrlByTag(tag: String): Single<String> {
        return giphyUrl
                .httpGet(listOf("api_key" to "dc6zaTOxFJmzC", "tag" to tag, "rating" to "r")) // The API key is public demo key, don't panic
                .rx_object(GiphyRandomResponse.Deserializer())
                .timeout(5, TimeUnit.SECONDS)
                .retry(1)
                .map { it.get().data.url }
    }
}