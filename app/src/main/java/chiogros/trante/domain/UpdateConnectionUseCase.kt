package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.repository.RoomManager

class UpdateConnectionUseCase(private val repository: RoomManager) {
    suspend operator fun invoke(con: Connection) {
        return repository.update(con)
    }
}