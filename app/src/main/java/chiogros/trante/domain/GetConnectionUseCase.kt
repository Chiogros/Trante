package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.Flow

class GetConnectionUseCase(private val repository: RoomManager) {
    operator fun invoke(id: String): Flow<Connection> {
        return repository.get(id)
    }
}