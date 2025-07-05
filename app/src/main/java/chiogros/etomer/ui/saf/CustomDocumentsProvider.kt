package chiogros.etomer.ui.saf

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import chiogros.etomer.R

class CustomDocumentsProvider : DocumentsProvider() {
    override fun openDocument(
        documentId: String?,
        mode: String?,
        signal: CancellationSignal?
    ): ParcelFileDescriptor? {
        TODO("Not yet implemented")
    }

    override fun queryChildDocuments(
        parentDocumentId: String?,
        projection: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        var column: Array<out String?>? = projection

        if (projection == null) {
            val columnNames = listOf<String>(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_FLAGS,
                DocumentsContract.Document.COLUMN_SIZE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            )
            column = Array<String>(columnNames.size, { index -> columnNames.get(index) })
        }
        return MatrixCursor(column).apply {
            newRow().apply {
                add("a")
                add("a")
                add("a")
                add("a")
                add(64)
                add(1749484283000)
            }
            newRow().apply {
                add("b")
                add("b")
                add("b")
                add("b")
                add(64)
                add(1749484283000)
            }
        }
    }

    override fun queryDocument(
        documentId: String?,
        projection: Array<out String?>?
    ): Cursor? {
        var column: Array<out String?>? = projection

        if (projection == null) {
            val columnNames = listOf<String>(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_FLAGS,
                DocumentsContract.Document.COLUMN_SIZE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            )
            column = Array<String>(columnNames.size, { index -> columnNames.get(index) })
        }

        val arr = MatrixCursor(column).apply {
            newRow().apply {
                add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, "root")
                add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, "root")
                add(
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.MIME_TYPE_DIR
                )
                add(DocumentsContract.Document.COLUMN_SIZE, null)
                add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, null)
            }
        }

        return arr
    }

    override fun queryRoots(projection: Array<out String?>?): Cursor? {
        var column: Array<out String?>? = projection

        if (projection == null) {
            val columnNames = listOf<String>(
                DocumentsContract.Root.COLUMN_TITLE,
                DocumentsContract.Root.COLUMN_ROOT_ID,
                DocumentsContract.Root.COLUMN_FLAGS,
                DocumentsContract.Root.COLUMN_DOCUMENT_ID,
                DocumentsContract.Root.COLUMN_ICON
            )
            column = Array<String>(columnNames.size, { index -> columnNames.get(index) })
        }
        val cursor = MatrixCursor(column)

        // If user is not logged in, return an empty root cursor.  This removes our
        // provider from the list entirely.
        /*if (!isUserLoggedIn()) {
            return result
        }*/

        val app_name = this.context?.getString(R.string.app_name)

        // It's possible to have multiple roots (e.g. for multiple accounts in the
        // same app) -- just add multiple cursor rows.
        cursor.newRow().apply {
            add(DocumentsContract.Root.COLUMN_TITLE, app_name)
            add(DocumentsContract.Root.COLUMN_ICON, R.drawable.ic_launcher)
            add(DocumentsContract.Root.COLUMN_ROOT_ID, "ROOT")
            add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, "root")
            add(
                DocumentsContract.Root.COLUMN_FLAGS, DocumentsContract.Root.FLAG_SUPPORTS_CREATE or
                        DocumentsContract.Root.FLAG_SUPPORTS_SEARCH
            )
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        return true
    }
}