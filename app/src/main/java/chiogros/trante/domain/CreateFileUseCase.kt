package chiogros.trante.domain

import chiogros.trante.data.network.repository.NetworkManager
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first

class CreateFileUseCase(
    private val repository: RoomManager,
    private val remote: NetworkManager
) {
    suspend operator fun invoke(id: String, path: String): Boolean {
        val con = repository.get(id).first()
        return remote.createFile(con, path)
    }
}