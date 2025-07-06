package chiogros.etomer.domain

import chiogros.etomer.data.remote.repository.RemoteManager
import chiogros.etomer.data.room.repository.ConnectionManager

class ListFilesInDirectoryUseCase(
    private val repository: ConnectionManager,
    private val remoteManager: RemoteManager
) {
    suspend operator fun invoke(conId: String, path: String) {
        val con = repository.get(conId)
        val files = remoteManager.listFiles(path)
    }
}