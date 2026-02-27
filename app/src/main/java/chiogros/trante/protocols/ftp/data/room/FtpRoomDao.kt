package chiogros.trante.protocols.ftp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FtpRoomDao {
    @Delete
    suspend fun delete(con: FtpRoom)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(con: FtpRoom)

    @Update
    suspend fun update(con: FtpRoom)

    @Query("SELECT * FROM FtpRoom WHERE id = :id")
    fun get(id: String): Flow<FtpRoom>

    @Query("SELECT * FROM FtpRoom")
    fun getAll(): Flow<List<FtpRoom>>
}