package chiogros.etomer.domain

import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.Flow

class GetConnectionsUseCase(private val repository: ConnectionManager) {
    operator fun invoke(): Flow<List<Connection>> {
        return repository.getAll()
    }
}