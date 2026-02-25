package chiogros.trante.data.room

import kotlinx.coroutines.flow.Flow

/** Commands available to exchange data with Room. */
interface RoomRepository {
    suspend fun delete(con: Connection)
    fun get(id: String): Flow<Connection>
    fun getAll(): Flow<List<Connection>>
    suspend fun insert(con: Connection)
    suspend fun update(con: Connection)
}