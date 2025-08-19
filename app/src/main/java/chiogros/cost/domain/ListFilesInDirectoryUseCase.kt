package chiogros.cost.domain

import chiogros.cost.data.remote.File
import chiogros.cost.data.remote.repository.RemoteManager
import chiogros.cost.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class ListFilesInDirectoryUseCase(
    private val repository: ConnectionManager,
    private val remoteManager: RemoteManager
) {
    suspend operator fun invoke(conId: String, path: String): List<File> {
        val con = repository.get(conId).first()

        if (!remoteManager.connect(con)) {
            return emptyList()
        }

        return remoteManager.listFiles(con, path)
    }
}