package chiogros.cost.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.cost.data.room.repository.RoomManager
import chiogros.cost.ui.saf.CustomDocumentsProvider
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