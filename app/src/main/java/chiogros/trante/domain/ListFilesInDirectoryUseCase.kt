package chiogros.trante.domain

import chiogros.trante.data.network.File
import chiogros.trante.data.network.repository.NetworkManager
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first

class ListFilesInDirectoryUseCase(
    private val repository: RoomManager,
    private val networkManager: NetworkManager
) {
    suspend operator fun invoke(conId: String, path: String): List<File> {
        val con = repository.get(conId).first()

        if (!networkManager.connect(con)) {
            return emptyList()
        }

        return networkManager.listFiles(con, path)
    }
}