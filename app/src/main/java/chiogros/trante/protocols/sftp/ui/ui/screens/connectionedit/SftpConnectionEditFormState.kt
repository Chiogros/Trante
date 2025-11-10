package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

data class SftpConnectionEditFormState(
    override val id: String = String(),
    override var name: String = String(),
    var host: String = String(),
    var user: String = String(),
    var password: String = String(),
    var showPassword: Boolean = false
) : ConnectionEditCommonFormState(id = id, name = name) {
    fun copy(
        id: String = this.name,
        name: String = this.name,
        host: String = this.host,
        user: String = this.user,
        password: String = this.password
    ) = SftpConnectionEditFormState(
        id = id,
        name = name,
        host = host,
        user = user,
        password = password
    )
}