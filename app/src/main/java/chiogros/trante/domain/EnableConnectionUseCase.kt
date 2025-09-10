package chiogros.trante.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.trante.BuildConfig
import chiogros.trante.data.network.repository.NetworkManager
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.data.room.repository.RoomManager
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
        val uri = buildRootsUri(BuildConfig.PACKAGE_NAME + BuildConfig.PROVIDER_NAME)
        context.contentResolver.notifyChange(uri, null)
    }
}