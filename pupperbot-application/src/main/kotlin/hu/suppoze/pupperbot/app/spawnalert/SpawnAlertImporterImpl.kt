package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnAlertSchedule
import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnEntry
import org.kodein.di.generic.instance
import java.io.FileReader
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SpawnAlertImporterImpl : SpawnAlertImporter {

    private val spawnAlertScheduleFileLocation: String by kodein.instance("spawnAlertScheduleFileLocation")

    override fun importSchedule(): SpawnAlertSchedule {
        val reader = FileReader(spawnAlertScheduleFileLocation)
        val lineSequence = reader.readLines()
            .asSequence()

        val times = lineSequence.first().split(',')
            .map {
                LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))
            }.toList()

        val spawnEntryList = lineSequence.drop(1).mapIndexed { dayIndex, line ->
            val bosses = line.split(',')
            if (bosses.size != times.size) throw RuntimeException("Malformed CSV")
            bosses.mapIndexed { timeIndex, boss ->
                SpawnEntry(DayOfWeek.of(dayIndex + 1), times[timeIndex], boss)
            }
        }.toList().flatten()

        return SpawnAlertSchedule(spawnEntryList)
    }

    override fun saveSchedule(schedule: SpawnAlertSchedule) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
