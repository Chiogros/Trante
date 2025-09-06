package chiogros.cost.data.room.crypto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity()
data class CryptoRoom(
    @PrimaryKey
    val id: String = Uuid.random().toString(),
    var ciphertext: String = "",
    var iv: String = "",
)