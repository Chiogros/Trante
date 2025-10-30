package chiogros.trante.ui.ui.screens.connectionedit

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.Protocol

/**
 * Screen level UI state
 */
data class ConnectionEditUiState(
    var deletedConnection: Connection,
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,

    val protocol: Protocol,
    val unmodifiedFormHash: Int,
    val isModified: Boolean = false
)
