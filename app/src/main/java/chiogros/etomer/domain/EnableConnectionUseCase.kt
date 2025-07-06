package chiogros.etomer.domain

import chiogros.etomer.data.remote.repository.RemoteManager
import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class EnableConnectionUseCase(
    private val repository: ConnectionManager,
    private val remote: RemoteManager
) {
    suspend operator fun invoke(id: String) {
        val con = repository.get(id).first()
        con.enabled = true
        repository.update(con)

        remote.connect(con)
    }
}