package hu.suppoze.pupperbot.rss

import io.reactivex.Observable

class RssDatabase {

    fun getTestEntry(): Observable<RssEntry> = Observable.fromIterable(RssEntry.all())

}