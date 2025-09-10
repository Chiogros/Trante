package chiogros.trante.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.trante.BuildConfig
import chiogros.trante.data.room.repository.RoomManager
import kotlinx.coroutines.flow.first

class DisableConnectionUseCase(
    private val repository: RoomManager,
    private val context: Context
) {
    suspend operator fun invoke(id: String) {
        val con = repository.get(id).first()
        con.enabled = false
        repository.update(con)

        // Notify ContentProvider about changes in enabled connections
        val uri = buildRootsUri(BuildConfig.PACKAGE_NAME + BuildConfig.PROVIDER_NAME)
        context.contentResolver.notifyChange(uri, null)
    }
}