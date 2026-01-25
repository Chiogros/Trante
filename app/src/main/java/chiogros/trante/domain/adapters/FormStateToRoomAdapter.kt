package chiogros.trante.domain.adapters

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.SftpConnectionEditFormState

interface FormStateToRoomAdapter {
    fun convert(formState: CommonConnectionEditFormState): Connection
    fun convert(con: Connection): SftpConnectionEditFormState
}