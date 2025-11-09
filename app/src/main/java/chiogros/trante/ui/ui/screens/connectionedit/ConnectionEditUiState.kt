package chiogros.trante.ui.ui.screens.connectionedit

import androidx.compose.runtime.Composable
import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.sftp.data.room.SftpRoom

data class ConnectionEditUiState(
    val formState: ConnectionEditCommonFormState = ConnectionEditCommonFormState(),
    // Holds initial form data, useful to check for changes
    val originalFormState: ConnectionEditCommonFormState = formState,
    val form: @Composable () -> Unit = {},
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,
    var deletedConnection: Connection = SftpRoom(),
    // Default protocol shown to the user
    val protocol: Protocol = Protocol.SFTP,
) {
    val isEdited: Boolean
        get() = formState != originalFormState
}
