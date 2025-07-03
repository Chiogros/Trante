package chiogros.etomer.data.repositories

import chiogros.etomer.data.datasource.ConnectionSftpRoomDataSource
import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.ConnectionSftp
import kotlinx.coroutines.flow.Flow

class ConnectionSftpRepository(private val localDataSource: ConnectionSftpRoomDataSource) :
    ConnectionRepository() {
    override suspend fun delete(connection: Connection) {
        delete(connection as ConnectionSftp)
    }

    suspend fun delete(connection: ConnectionSftp) {
        localDataSource.delete(connection)
    }

    override fun get(id: String): Flow<ConnectionSftp> {
        return localDataSource.get(id)
    }

    override fun getAll(): Flow<List<ConnectionSftp>> {
        return localDataSource.getAll()
    }

    override suspend fun insert(connection: Connection) {
        insert(connection as ConnectionSftp)
    }

    suspend fun insert(connection: ConnectionSftp) {
        localDataSource.insert(connection)
    }

    override suspend fun update(connection: Connection) {
        update(connection as ConnectionSftp)
    }

    suspend fun update(connection: ConnectionSftp) {
        localDataSource.update(connection)
    }
}
