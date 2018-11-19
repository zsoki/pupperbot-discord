package hu.suppoze.pupperbot.app.command.spawnalert.repository

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ScheduleStringTable : IntIdTable() {
    val timestamp = long("timestamp")
    val scheduleString= text("scheduleString")
}

class ScheduleStringEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ScheduleStringEntity>(ScheduleStringTable)

    var timestamp by ScheduleStringTable.timestamp
    var scheduleString by ScheduleStringTable.scheduleString
}