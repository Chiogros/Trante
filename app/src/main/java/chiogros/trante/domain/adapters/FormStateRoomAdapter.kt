package chiogros.trante.domain.adapters

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.FormState
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditFormState

abstract class FormStateRoomAdapter {
    abstract fun convert(formState: ConnectionEditFormState): Connection
    abstract fun convert(con: Connection): FormState
}