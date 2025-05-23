package chiogros.etomer.data.storage

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConnectionSftp(
    @PrimaryKey(autoGenerate = true)
    val connectionSftpId: Long,
    @Embedded
    val connectionNetwork: ConnectionNetwork
)

@Entity
data class ConnectionNetwork(
    @PrimaryKey(autoGenerate = true)
    val connectionNetworkId: Long,
    val host: String,
    val user: String,
    @Embedded
    val connection: Connection
)

@Entity
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val connectionId: Long = 0,
    var enabled: Boolean,
)