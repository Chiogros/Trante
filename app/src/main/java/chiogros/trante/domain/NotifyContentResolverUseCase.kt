package chiogros.trante.domain

import android.content.Context
import android.provider.DocumentsContract.buildRootsUri
import chiogros.trante.BuildConfig

class NotifyContentResolverUseCase(private val context: Context) {
    operator fun invoke() {
        // Notify ContentProvider about changes in enabled connections
        val uri = buildRootsUri(BuildConfig.PROVIDER_NAME)
        context.contentResolver.notifyChange(uri, null)
    }
}