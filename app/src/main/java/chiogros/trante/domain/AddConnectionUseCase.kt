package chiogros.trante.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.repository.RoomManager

class AddConnectionUseCase(private val repository: RoomManager) {
    suspend operator fun invoke(con: Connection) {
        return repository.insert(con)
    }
}