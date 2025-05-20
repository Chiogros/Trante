package chiogros.etomer.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val enabled: Boolean
)