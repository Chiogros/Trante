package chiogros.etomer.domain

import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class DisableConnectionUseCase(private val repository: ConnectionManager) {
    suspend operator fun invoke(id: String) {
        val con = repository.get(id).first()
        con.enabled = false
        repository.update(con)
    }
}