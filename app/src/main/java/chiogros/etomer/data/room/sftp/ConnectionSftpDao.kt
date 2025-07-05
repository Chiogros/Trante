package chiogros.etomer.data.room.sftp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionSftpDao {
    @Delete
    suspend fun delete(con: ConnectionSftp)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(con: ConnectionSftp)

    @Update
    suspend fun update(con: ConnectionSftp)

    @Query("SELECT * FROM ConnectionSftp WHERE id = :id")
    fun get(id: String): Flow<ConnectionSftp>

    @Query("SELECT * FROM ConnectionSftp")
    fun getAll(): Flow<List<ConnectionSftp>>
}