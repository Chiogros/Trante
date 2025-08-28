package chiogros.cost.domain

import chiogros.cost.data.remote.repository.RemoteManager
import chiogros.cost.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first
import java.io.ByteArrayInputStream
import java.io.InputStream

class ReadFileUseCase(
    private val repository: ConnectionManager,
    private val remoteManager: RemoteManager
) {
    suspend operator fun invoke(conId: String, path: String): InputStream {
        val con = repository.get(conId).first()
        if (!remoteManager.connect(con)) {
            return ByteArrayInputStream(ByteArray(0))
        }
        return remoteManager.readFile(con, path)
    }
}