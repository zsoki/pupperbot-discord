package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnAlertSchedule

interface SpawnAlertImporter {

    fun loadSchedule(): SpawnAlertSchedule

    fun saveSchedule(schedule: SpawnAlertSchedule)

}