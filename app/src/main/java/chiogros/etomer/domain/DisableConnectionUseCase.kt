package chiogros.etomer.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.etomer.data.room.repository.ConnectionManager
import kotlinx.coroutines.flow.first

class DisableConnectionUseCase(
    private val repository: ConnectionManager,
    private val context: Context
) {
    suspend operator fun invoke(id: String) {
        val con = repository.get(id).first()
        con.enabled = false
        repository.update(con)

        // Notify ContentProvider about changes in enabled connections
        val uri = buildRootsUri("chiogros.etomer.data.saf.documents")
        context.contentResolver.notifyChange(uri, null)
    }
}