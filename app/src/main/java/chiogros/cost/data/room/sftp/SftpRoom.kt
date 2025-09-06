package chiogros.cost.data.room.sftp

import androidx.room.Entity
import androidx.room.PrimaryKey
import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.ConnectionState
import chiogros.cost.data.room.crypto.EncryptedData
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity
data class SftpRoom(
    @PrimaryKey
    override val id: String = Uuid.random().toString(),
    override var name: String = "",
    override var enabled: Boolean = false,
    override var host: String = "",
    override var user: String = "",
    override var state: ConnectionState = ConnectionState.NEVER_USED,
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