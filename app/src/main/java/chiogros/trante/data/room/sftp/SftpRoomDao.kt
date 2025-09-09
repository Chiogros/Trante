package chiogros.trante.data.room.sftp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SftpRoomDao {
    @Delete
    suspend fun delete(con: SftpRoom)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(con: SftpRoom)

    @Update
    suspend fun update(con: SftpRoom)

    @Query("SELECT * FROM SftpRoom WHERE id = :id")
    fun get(id: String): Flow<SftpRoom>

    @Query("SELECT * FROM SftpRoom")
    fun getAll(): Flow<List<SftpRoom>>
}