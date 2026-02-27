package chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit

import chiogros.trante.protocols.common.CommonConnectionEditFormState

data class FtpConnectionEditFormState(
    override val id: String = String(),
    override var name: String = String(),
    var host: String = String(),
    var user: String = String(),
    var password: String = String(),
) : CommonConnectionEditFormState