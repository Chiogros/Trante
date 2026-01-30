package chiogros.trante.ui.ui.screens.connectionedit

import chiogros.trante.protocols.Protocol
import chiogros.trante.protocols.common.CommonConnectionEditFormState

/**
 * Screen level UI state
 */
data class ConnectionEditUiState(
    var deletedConnection: CommonConnectionEditFormState,
    val isEditing: Boolean = false,
    val isDialogShown: Boolean = false,

    val protocol: Protocol,
    val unmodifiedFormHash: Int,
    val isModified: Boolean = false
)
