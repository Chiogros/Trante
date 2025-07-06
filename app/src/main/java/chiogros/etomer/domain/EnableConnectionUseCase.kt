package chiogros.etomer.domain

import chiogros.etomer.data.remote.repository.RemoteManager
import chiogros.etomer.data.room.ConnectionState
import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class EnableConnectionUseCase(
    private val repository: ConnectionManager,
    private val remote: RemoteManager
) {
    suspend operator fun invoke(id: String) {
        val con = repository.get(id).first()

        // Save ongoing action is room
        con.enabled = true
        con.state = ConnectionState.CONNECTING
        repository.update(con)

        // Try to connect and update state accordingly
        if (remote.connect(con)) {
            con.state = ConnectionState.CONNECTED
        } else {
            con.state = ConnectionState.FAILED
        }
        repository.update(con)
    }
}