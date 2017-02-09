package hu.suppoze.pupperbot.giphy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import rx.Observable

class GiphyServer {

    fun requestRandomGiphyUrlByTag(tag: String): Observable<String> {
        return "http://api.giphy.com/v1/gifs/random"
                .httpGet(listOf("api_key" to "dc6zaTOxFJmzC", "tag" to tag, "rating" to "r"))
                .rx_object(GiphyRandomResponse.Deserializer())
                .retry(1)
                .map { it.data.url }
    }
}