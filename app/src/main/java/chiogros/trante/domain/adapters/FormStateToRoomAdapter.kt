package chiogros.trante.domain.adapters

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.ConnectionEditFormStateSftp
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

abstract class FormStateToRoomAdapter {
    abstract fun convert(formState: ConnectionEditCommonFormState): Connection
    abstract fun convert(con: Connection): ConnectionEditFormStateSftp
}