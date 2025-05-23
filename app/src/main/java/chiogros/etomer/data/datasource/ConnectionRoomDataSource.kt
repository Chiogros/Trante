package chiogros.etomer.data.datasource

import chiogros.etomer.data.storage.Connection
import chiogros.etomer.data.storage.ConnectionDao
import kotlinx.coroutines.flow.Flow

class ConnectionRoomDataSource(private val dao: ConnectionDao) {
    fun getAll(): Flow<List<Connection>> {
        return dao.getAll()
    }

    suspend fun insert(connection: Connection) {
        dao.insert(connection)
    }

    suspend fun update(connection: Connection) {
        dao.update(connection)
    }
}