package chiogros.cost.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.cost.data.network.repository.NetworkManager
import chiogros.cost.data.room.ConnectionState
import chiogros.cost.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first

class EnableConnectionUseCase(
    private val repository: RoomManager,
    private val remote: NetworkManager,
    private val context: Context
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
        // Notify ContentProvider about changes in enabled connections
        val uri = buildRootsUri("chiogros.cost.ui.saf.CustomDocumentsProvider")
        context.contentResolver.notifyChange(uri, null)
    }
}