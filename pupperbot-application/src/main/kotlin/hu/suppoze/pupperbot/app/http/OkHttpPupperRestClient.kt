package hu.suppoze.pupperbot.app.http

import hu.suppoze.pupperbot.app.util.withPathParams
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager

class OkHttpPupperRestClient : PupperRestClient {

    private val cookieHandler: CookieHandler = CookieManager()
    private val restClient: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieHandler))
        .build()

    override fun get(
        url: String,
        params: List<Pair<String, Any>>?,
        vararg pathParams: String
    ): String {
        val replacedUrl: String = url.withPathParams(*pathParams)

        val httpUrl = replacedUrl.toHttpUrlOrNull()!!.newBuilder()
            .addAllQueryParams(params)
            .build()

        val request = Request.Builder()
            .url(httpUrl)
            .build()

        restClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }
    }

    private fun HttpUrl.Builder.addAllQueryParams(params: List<Pair<String, Any>>?): HttpUrl.Builder {
        if (params == null || params.isEmpty()) return this
        params.forEach { this.addQueryParameter(it.first, it.second.toString()) }
        return this
    }

}