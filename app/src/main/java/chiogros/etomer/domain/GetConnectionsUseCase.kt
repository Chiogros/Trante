package chiogros.etomer.domain

import chiogros.etomer.data.repositories.room.ConnectionManager
import chiogros.etomer.data.room.Connection
import kotlinx.coroutines.flow.Flow

class GetConnectionsUseCase(private val repository: ConnectionManager) {
    operator fun invoke(): Flow<List<Connection>> {
        return repository.getAll()
    }
}