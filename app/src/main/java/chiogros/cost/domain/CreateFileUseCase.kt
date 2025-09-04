package chiogros.cost.domain

import chiogros.cost.data.remote.repository.RemoteManager
import chiogros.cost.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class CreateFileUseCase(
    private val repository: ConnectionManager,
    private val remote: RemoteManager
) {
    suspend operator fun invoke(id: String, path: String): Boolean {
        val con = repository.get(id).first()
        return remote.createFile(con, path)
    }
}