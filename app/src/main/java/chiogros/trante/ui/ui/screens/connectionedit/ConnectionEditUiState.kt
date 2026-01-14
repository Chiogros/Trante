package chiogros.trante.ui.ui.screens.connectionedit

import androidx.compose.runtime.Composable
import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import chiogros.trante.ui.ui.screens.connectionedit.form.ConnectionEditFormState

/**
 * Screen level UI state
 */
data class ConnectionEditUiState(
    var deletedConnection: Connection = SftpRoom(),
    val gui: @Composable (() -> Unit) = {},
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,

    // Default protocol shown to the user
    val protocol: Protocol = Protocol.SFTP,
    val protocolForm: ConnectionEditFormState
) {
    val isModified: Boolean
        get() = protocolForm.isModified
}
