package hu.suppoze.pupperbot.common.http

import com.github.kittinunf.fuel.Fuel

// TODO: finish abstraction
abstract class FuelRestClient(
    protected val url: String = "",
    protected val timeout: Int = 3000
) : RestClient {

//    fun <reified ResponseType : Any>
//            buildGetRequest(urlParams: List<Pair<String, Any>>? = null,
//                            pathParams: List<Pair<String, Any>>? = null
//    ): ResponseType {
//
//        if (pathParams != null) {
//
//        }
//
//        val (_, _, result) =
//                Fuel.get(url, urlParams)
//                    .timeout(timeout)
//                    .re<ResponseType>()
//
//        val (response, fuelError) = result
//
//        if (response == null)
//            throw fuelError!!.exception
//
//        return response
//    }

}
