package chiogros.etomer.data.repositories

import chiogros.etomer.data.datasource.ConnectionRoomDataSource
import chiogros.etomer.data.storage.Connection
import kotlinx.coroutines.flow.Flow

class ConnectionRepository(private val localDataSource: ConnectionRoomDataSource) {
    fun getAll(): Flow<List<Connection>> {
        return localDataSource.getAll()
    }

    suspend fun update(connection: Connection) {
        localDataSource.update(connection)
    }
}
