package chiogros.etomer.domain

import chiogros.etomer.data.remote.File
import chiogros.etomer.data.remote.repository.RemoteManager
import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class ListFilesInDirectoryUseCase(
    private val repository: ConnectionManager,
    private val remoteManager: RemoteManager
) {
    suspend operator fun invoke(conId: String, path: String): List<File> {
        val con = repository.get(conId).first()
        remoteManager.connect(con)
        return remoteManager.listFiles(con, path)
    }
}