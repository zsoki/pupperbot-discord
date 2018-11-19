package hu.suppoze.pupperbot.app.command.spawnalert

import hu.suppoze.pupperbot.app.command.spawnalert.domain.SpawnAlertSchedule

interface SpawnAlertImporter {

    fun loadSchedule(): SpawnAlertSchedule

    fun saveSchedule(schedule: SpawnAlertSchedule)

}