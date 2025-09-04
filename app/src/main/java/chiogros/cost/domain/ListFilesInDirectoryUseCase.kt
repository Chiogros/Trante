package chiogros.cost.domain

import chiogros.cost.data.network.File
import chiogros.cost.data.network.repository.NetworkManager
import chiogros.cost.data.room.repository.RoomManager
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