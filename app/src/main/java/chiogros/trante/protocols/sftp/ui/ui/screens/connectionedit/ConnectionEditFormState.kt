package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditFormState

class FormState(
    override val id: String = String(),
    val host: String = String(),
    val name: String = String(),
    val user: String = String(),
    val password: String = String()
) : ConnectionEditFormState(id)