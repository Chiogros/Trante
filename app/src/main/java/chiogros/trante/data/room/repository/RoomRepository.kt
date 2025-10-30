package chiogros.trante.data.room.repository

import chiogros.trante.data.room.Connection
import kotlinx.coroutines.flow.Flow

abstract class RoomRepository() {
    abstract suspend fun delete(con: Connection)
    abstract fun get(id: String): Flow<Connection>
    abstract fun getAll(): Flow<List<Connection>>
    abstract suspend fun insert(con: Connection)
    abstract suspend fun update(con: Connection)
}
