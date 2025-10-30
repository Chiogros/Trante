package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.Flow

class GetConnectionsUseCase(private val repository: RoomManager) {
    operator fun invoke(): Flow<List<Connection>> {
        return repository.getAll()
    }
}