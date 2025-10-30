package chiogros.trante.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.trante.data.room.repository.RoomManager
import chiogros.trante.ui.saf.CustomDocumentsProvider
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
        val uri = buildRootsUri(CustomDocumentsProvider().javaClass.name)
        context.contentResolver.notifyChange(uri, null)
    }
}