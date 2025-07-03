package chiogros.etomer.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class ConnectionSftp(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
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