package hu.suppoze.pupperbot.common

interface Command<in T> {

    val onNext: (T) -> Unit

    val onError: (Throwable) -> Unit

    fun perform()
}