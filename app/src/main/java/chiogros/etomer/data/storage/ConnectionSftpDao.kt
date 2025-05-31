package chiogros.etomer.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionSftpDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(con: ConnectionSftp)

    @Update
    suspend fun update(con: ConnectionSftp)

    @Delete
    suspend fun delete(con: ConnectionSftp)

    @Query("SELECT * from ConnectionSftp")
    fun getAll(): Flow<List<ConnectionSftp>>
}