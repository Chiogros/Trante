package chiogros.etomer.data.repositories

import chiogros.etomer.data.room.Connection
import kotlinx.coroutines.flow.Flow

abstract class ConnectionRepository() {
    abstract suspend fun delete(connection: Connection)
    abstract fun get(id: String): Flow<Connection>
    abstract fun getAll(): Flow<List<Connection>>
    abstract suspend fun insert(connection: Connection)
    abstract suspend fun update(connection: Connection)
}
