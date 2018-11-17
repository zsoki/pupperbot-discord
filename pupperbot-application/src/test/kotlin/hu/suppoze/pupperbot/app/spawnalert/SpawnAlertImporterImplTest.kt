package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.di.kodein
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance

internal class SpawnAlertImporterImplTest {

    private val spawnAlertImporter: SpawnAlertImporter by kodein.instance()

    @Test
    fun importSchedule() {
        spawnAlertImporter.loadSchedule()
    }
}