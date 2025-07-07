package chiogros.etomer.domain

import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetEnabledConnectionsUseCase(private val repository: ConnectionManager) {
    operator fun invoke(): List<Connection> {
        var enabledConnections: List<Connection> = emptyList()

        runBlocking {
            enabledConnections = repository.getAll().first()
        }

        return enabledConnections.filter { it.enabled }
    }
}