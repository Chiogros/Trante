package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetEnabledConnectionsUseCase(private val repository: RoomManager) {
    operator fun invoke(): List<Connection> {
        var enabledConnections: List<Connection> = emptyList()

        runBlocking {
            enabledConnections = repository.getAll().first()
        }

        return enabledConnections.filter { it.enabled }
    }
}