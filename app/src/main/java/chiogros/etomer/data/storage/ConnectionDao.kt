package chiogros.etomer.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(con: Connection)

    @Update
    suspend fun update(con: Connection)

    @Delete
    suspend fun delete(con: Connection)

    @Query("SELECT * from Connection WHERE id = :id")
    fun get(id: Int): Flow<Connection>

    @Query("SELECT * from Connection ORDER BY id ASC")
    fun getAll(): Flow<List<Connection>>
}