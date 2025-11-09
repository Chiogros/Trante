package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

class ConnectionEditFormStateSftp(
    id: String = String(),
    name: String = String(),
    var host: String = String(),
    var user: String = String(),
    var password: String = String(),
    var showPassword: Boolean = false
) : ConnectionEditCommonFormState(id = id, name = name)