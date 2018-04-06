package hu.suppoze.pupperbot.common.http

interface RestClient {

    fun buildGetRequest(
        urlParams: List<Pair<String, Any>>? = null,
        pathParams: List<Pair<String, Any>>? = null
    ): Any

}