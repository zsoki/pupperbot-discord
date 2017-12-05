package hu.suppoze.pupperbot.cinema

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.runBlocking
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CinemaService {

    private val szegedLocationId = 1010214

    fun fetchSchedule() = runBlocking {
        val scheduleContext = newSingleThreadContext("ScheduleContext")
        val channel = Channel<List<Pair<Movie, List<LocalDateTime>>>>()
        val schedule = Schedule(szegedLocationId, 1, HashMap())

        (0..7).forEach { launch(scheduleContext) { channel.send(getMoviesForDay(it)) } }
        (0..7).forEach {
            val pairList = channel.receive()
            pairList.forEach {
                if (!schedule.screenings.containsKey(it.first)) {
                    schedule.screenings.put(it.first, mutableListOf())
                }
                schedule.screenings[it.first]?.addAll(it.second)
            }
        }

        schedule
    }

    private fun getMoviesForDay(dayCount: Int): List<Pair<Movie, List<LocalDateTime>>> {
        val date = LocalDate.now().plusDays(dayCount.toLong())
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val document = getScheduleInfoRowsDocument(formattedDate)
        return extractMoviesFrom(document, date)
    }

    private fun getScheduleInfoRowsDocument(formattedDate: String): Document {
        val deficientBody = Jsoup.connect("http://www.cinemacity.hu/scheduleInfoRows")
                .data(mutableMapOf(
                        "locationId" to szegedLocationId.toString(),
                        "date" to formattedDate,
                        "venueTypeId" to "1"))
                .method(Connection.Method.POST)
                .execute()
                .body()
        val body = "<html><head></head><body><table>$deficientBody</table></html>"

        return Jsoup.parse(body)
    }

    private fun extractMoviesFrom(document: Document, date: LocalDate) =
            document.select("tbody > tr")
                    .filterNot { it.childNodeSize() < 5 }
                    .map {

                        val movie = Movie(
                                title = it.child(0).text(),
                                type = it.child(2).ownText(),
                                language = it.child(3).ownText(),
                                length = it.child(4).text()
                        )

                        val screeningDateTimes = it.select("td > a.presentationLink")
                                .filter { it.hasText() }
                                .map { LocalTime.parse(it.text()).atDate(date) }
                                .toList()

                        Pair(movie, screeningDateTimes)
                    }
}