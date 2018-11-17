package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.data.SqliteContext
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnAlertSchedule
import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnEntry
import hu.suppoze.pupperbot.app.spawnalert.repository.ScheduleStringEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.generic.instance
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SpawnAlertImporterImpl : SpawnAlertImporter {

    private val spawnAlertScheduleFileLocation: String by kodein.instance("spawnAlertScheduleFileLocation")

    override fun loadSchedule(): SpawnAlertSchedule {
        var scheduleString = getLatestScheduleFromDb()

        if (scheduleString == null) {
            scheduleString = loadDefaultSchedule()
        }

        val times = scheduleString.lineSequence()
            .first()
            .split(',')
            .map { parseTime(it) }
            .toList()

        val spawnEntryList = scheduleString.lineSequence()
            .drop(1)
            .mapIndexed { dayIndex, line -> parseBosses(line, times, dayIndex) }
            .toList()
            .flatten()

        return SpawnAlertSchedule(spawnEntryList)
    }

    override fun saveSchedule(schedule: SpawnAlertSchedule) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getLatestScheduleFromDb(): String? {
        return transaction(SqliteContext.db) {
            ScheduleStringEntity.all()
                .sortedByDescending { it.timestamp }
                .firstOrNull()
                ?.scheduleString
        }
    }

    private fun loadDefaultSchedule(): String {
        val defaultScheduleString = this.javaClass.getResource(spawnAlertScheduleFileLocation).readText()

        transaction(SqliteContext.db) {
            ScheduleStringEntity.new {
                this.timestamp = System.currentTimeMillis()
                this.scheduleString = defaultScheduleString
            }
        }
        return defaultScheduleString
    }

    private fun parseTime(it: String) = LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))

    private fun parseBosses(line: String, times: List<LocalTime>, dayIndex: Int): List<SpawnEntry> {
        val bosses = line.split(',')
        if (bosses.size != times.size) throw RuntimeException("Malformed CSV")
        return bosses.mapIndexed { timeIndex, boss ->
            SpawnEntry(DayOfWeek.of(dayIndex + 1), times[timeIndex], boss)
        }
    }

}

