package hu.suppoze.pupperbot.rss

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import rx.Observable
import java.io.InputStreamReader
import java.net.URL

class RssService() {

    fun getFeed(): Observable<SyndFeed> {
        return Observable.create<SyndFeed> {
            try {
                val feed = SyndFeedInput().build(InputStreamReader(URL("http://prog.hu/site/backend/proghu-rss.xml").openStream()))
                it.onNext(feed)
            } catch(t: Throwable) {
                it.onError(t)
            }
            it.onCompleted()
        }
    }

}