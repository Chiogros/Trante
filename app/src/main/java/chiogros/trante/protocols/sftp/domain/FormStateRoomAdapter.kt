package chiogros.trante.protocols.sftp.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.domain.adapters.FormStateRoomAdapter
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.FormState
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditFormState

class FormStateRoomAdapter : FormStateRoomAdapter() {
    fun convert(formState: FormState): SftpRoom {
        return SftpRoom(
            id = formState.id,
            name = formState.name,
            host = formState.host,
            user = formState.user,
            password = CryptoUtils().encrypt(formState.password.toByteArray())
        )
    }

    override fun convert(formState: ConnectionEditFormState): Connection {
        if (formState !is FormState) {
            throw ClassCastException()
        }

        return convert(formState)
    }

    override fun convert(con: Connection): FormState {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }

        return convert(con)
    }

    fun convert(con: SftpRoom): FormState {
        return FormState(
            id = con.id,
            name = con.name,
            host = con.host,
            user = con.user,
            password = String(CryptoUtils().decrypt(con.password))
        )
    }
}