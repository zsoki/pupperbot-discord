package hu.suppoze.pupperbot.common

interface UseCase<in T> {

    val onNext: (T) -> Unit

    val onError: (Throwable) -> Unit

    fun execute()
}