package chiogros.trante.protocols.ftp.domain

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.domain.adapters.FormStateToRoomAdapter
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import chiogros.trante.protocols.ftp.data.room.FtpRoom
import chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit.FtpConnectionEditFormState

class FtpFormStateToRoomAdapter : FormStateToRoomAdapter {
    fun convert(ftpConnectionEditFormState: FtpConnectionEditFormState): FtpRoom {
        val con = FtpRoom(
            name = ftpConnectionEditFormState.name,
            host = ftpConnectionEditFormState.host,
            user = ftpConnectionEditFormState.user,
            password = CryptoUtils().encrypt(ftpConnectionEditFormState.password.toByteArray())
        )

        if (ftpConnectionEditFormState.id.isNotEmpty()) {
            con.id = ftpConnectionEditFormState.id
        }

        return con
    }

    override fun convert(formState: CommonConnectionEditFormState): Connection {
        if (formState !is FtpConnectionEditFormState) {
            throw ClassCastException()
        }

        return convert(formState)
    }

    override fun convert(con: Connection): FtpConnectionEditFormState {
        if (con !is FtpRoom) {
            throw ClassCastException()
        }

        return convert(con)
    }

    fun convert(con: FtpRoom): FtpConnectionEditFormState {
        return FtpConnectionEditFormState(
            id = con.id,
            name = con.name,
            host = con.host,
            user = con.user,
            password = String(CryptoUtils().decrypt(con.password))
        )
    }
}