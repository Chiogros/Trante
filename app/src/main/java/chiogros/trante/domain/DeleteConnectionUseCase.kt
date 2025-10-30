package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first

class DeleteConnectionUseCase(private val repository: RoomManager) {
    suspend operator fun invoke(id: String) {
        val con: Connection = repository.get(id).first()
        return repository.delete(con)
    }
}