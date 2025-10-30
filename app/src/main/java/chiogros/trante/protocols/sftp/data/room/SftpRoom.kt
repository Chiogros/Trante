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
    override val id: String = Uuid.random().toString(),
    override var name: String = "",
    override var enabled: Boolean = false,
    override var host: String = "",
    override var user: String = "",
    override var state: ConnectionState = ConnectionState.NEVER_USED,
    @Embedded("password_")
    override var password: EncryptedData = EncryptedData()
) : Connection() {

    companion object {
        override fun toString(): String {
            return "SFTP"
        }
    }

    override fun toString(): String {
        return SftpRoom.toString()
    }
}