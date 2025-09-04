package chiogros.cost.domain

import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.repository.RoomManager
import kotlinx.coroutines.flow.Flow

class GetConnectionsUseCase(private val repository: RoomManager) {
    operator fun invoke(): Flow<List<Connection>> {
        return repository.getAll()
    }
}