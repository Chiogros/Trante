package chiogros.trante.ui.ui.screens.connectionedit.form

import chiogros.trante.protocols.common.CommonConnectionEditFormState

/**
 * Form level state, embeds both common fields and protocol-specific fields.
 */
data class ConnectionEditFormState(
    val state: CommonConnectionEditFormState,
    val unmodifiedState: CommonConnectionEditFormState = state,
) {
    val isModified: Boolean
        get() = state != unmodifiedState
}