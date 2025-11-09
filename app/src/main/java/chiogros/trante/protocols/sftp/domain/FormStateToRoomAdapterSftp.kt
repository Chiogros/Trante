package chiogros.trante.protocols.sftp.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.ConnectionEditFormStateSftp
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditCommonFormState

class FormStateToRoomAdapterSftp : FormStateToRoomAdapter() {
    fun convert(connectionEditFormStateSftp: ConnectionEditFormStateSftp): SftpRoom {
        val con = SftpRoom(
            name = connectionEditFormStateSftp.name,
            host = connectionEditFormStateSftp.host,
            user = connectionEditFormStateSftp.user,
            password = CryptoUtils().encrypt(connectionEditFormStateSftp.password.toByteArray())
        )

        if (connectionEditFormStateSftp.id.isNotBlank()) {
            con.id = connectionEditFormStateSftp.id
        }

        return con
    }

    override fun convert(formState: ConnectionEditCommonFormState): Connection {
        if (formState !is ConnectionEditFormStateSftp) {
            throw ClassCastException()
        }

        return convert(formState)
    }

    override fun convert(con: Connection): ConnectionEditFormStateSftp {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }

        return convert(con)
    }

    fun convert(con: SftpRoom): ConnectionEditFormStateSftp {
        return ConnectionEditFormStateSftp(
            id = con.id,
            name = con.name,
            host = con.host,
            user = con.user,
            password = String(CryptoUtils().decrypt(con.password))
        )
    }
}