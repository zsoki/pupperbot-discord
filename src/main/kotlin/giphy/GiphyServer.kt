package giphy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class GiphyServer {

    fun requestRandomGiphyUrlByTag(tag: String,
                                   onSuccess: (String) -> Unit,
                                   onFailure: () -> Unit) {
        "http://api.giphy.com/v1/gifs/random"
                .httpGet(listOf("api_key" to "dc6zaTOxFJmzC", "tag" to tag, "rating" to "r"))
                .responseObject(GiphyRandomResponse.Deserializer()) { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            println(result.error.localizedMessage)
                            onFailure()
                        }
                        is Result.Success -> {
                            onSuccess(result.value.data.image_original_url)
                        }
                    }
                }
    }

}