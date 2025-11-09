package chiogros.trante.protocols.sftp.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.ConnectionState
import chiogros.trante.data.room.crypto.EncryptedData
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity
data class SftpRoom(
    @PrimaryKey
    // UUID is used to avoid content guessing (ex: content://chiogros.trante/<id>/document.txt)
    override var id: String = Uuid.random().toString(),
    override var name: String = String(),
    override var enabled: Boolean = false,
    override var state: ConnectionState = ConnectionState.NEVER_USED,
    var host: String = String(),
    var user: String = String(),
    @Embedded("password_")
    var password: EncryptedData = EncryptedData()
) : Connection() {

    override fun toString(): String {
        var stringyfied = super.toString()

        if (host.isNotEmpty() and user.isNotEmpty()) {
            stringyfied = user + '@' + host
        }

        return stringyfied
    }
}