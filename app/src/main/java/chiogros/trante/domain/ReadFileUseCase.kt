package chiogros.trante.domain

import chiogros.trante.data.network.repository.NetworkManager
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first
import java.io.ByteArrayInputStream
import java.io.InputStream

class ReadFileUseCase(
    private val repository: RoomManager,
    private val networkManager: NetworkManager
) {
    suspend operator fun invoke(conId: String, path: String): InputStream {
        val con = repository.get(conId).first()
        if (!networkManager.connect(con)) {
            return ByteArrayInputStream(ByteArray(0))
        }
        return networkManager.readFile(con, path)
    }
}