package chiogros.trante.protocols.sftp.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.SftpConnectionEditFormState
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

class SftpFormStateToRoomAdapter : FormStateToRoomAdapter() {
    fun convert(sftpConnectionEditFormState: SftpConnectionEditFormState): SftpRoom {
        val con = SftpRoom(
            name = sftpConnectionEditFormState.name,
            host = sftpConnectionEditFormState.host,
            user = sftpConnectionEditFormState.user,
            password = CryptoUtils().encrypt(sftpConnectionEditFormState.password.toByteArray())
        )

        if (sftpConnectionEditFormState.id.isNotBlank()) {
            con.id = sftpConnectionEditFormState.id
        }

        return con
    }

    override fun convert(formState: ConnectionEditCommonFormState): Connection {
        if (formState !is SftpConnectionEditFormState) {
            throw ClassCastException()
        }

        return convert(formState)
    }

    override fun convert(con: Connection): SftpConnectionEditFormState {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }

        return convert(con)
    }

    fun convert(con: SftpRoom): SftpConnectionEditFormState {
        return SftpConnectionEditFormState(
            id = con.id,
            name = con.name,
            host = con.host,
            user = con.user,
            password = String(CryptoUtils().decrypt(con.password))
        )
    }
}