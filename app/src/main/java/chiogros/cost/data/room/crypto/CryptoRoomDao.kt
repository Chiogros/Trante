package chiogros.cost.data.room.crypto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoRoomDao {
    @Delete
    suspend fun delete(con: CryptoRoom)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(con: CryptoRoom)

    @Update
    suspend fun update(con: CryptoRoom)

    @Query("SELECT * FROM CryptoRoom WHERE id = :id")
    fun get(id: String): Flow<CryptoRoom>
}