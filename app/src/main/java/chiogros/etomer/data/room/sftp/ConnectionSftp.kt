package chiogros.etomer.data.room.sftp

import androidx.room.Entity
import androidx.room.PrimaryKey
import chiogros.etomer.data.room.Connection
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity()
data class ConnectionSftp(
    @PrimaryKey
    override val id: String = Uuid.random().toString(),
    override var name: String = "",
    override var enabled: Boolean = false,
    override var host: String = "",
    override var user: String = "",
) : Connection() {
    companion object {
        override fun toString(): String {
            return "SFTP"
        }
    }

    override fun toString(): String {
        return ConnectionSftp.toString()
    }
}