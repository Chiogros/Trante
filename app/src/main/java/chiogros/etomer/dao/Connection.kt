package chiogros.etomer.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val enabled: Boolean
)