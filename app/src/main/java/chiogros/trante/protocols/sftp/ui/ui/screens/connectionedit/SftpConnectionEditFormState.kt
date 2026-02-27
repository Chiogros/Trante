package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import chiogros.trante.protocols.common.CommonConnectionEditFormState

data class SftpConnectionEditFormState(
    override val id: String = String(),
    override var name: String = String(),
    var host: String = String(),
    var user: String = String(),
    var password: String = String(),
) : CommonConnectionEditFormState