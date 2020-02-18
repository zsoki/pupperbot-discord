package hu.suppoze.pupperbot.app.http

interface PupperRestClient {

    fun get(url: String, params: List<Pair<String, Any>>? = null, vararg pathParams: String): String

}