package chiogros.etomer.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConnectionSftp(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String = "",
    var enabled: Boolean = false,
    val host: String,
    val user: String,
)