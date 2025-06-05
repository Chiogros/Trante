package chiogros.etomer.data.repositories

import chiogros.etomer.data.datasource.ConnectionSftpRoomDataSource
import chiogros.etomer.data.storage.ConnectionSftp
import kotlinx.coroutines.flow.Flow

class ConnectionSftpRepository(private val localDataSource: ConnectionSftpRoomDataSource) {
    suspend fun delete(connection: ConnectionSftp) {
        localDataSource.delete(connection)
    }

    fun get(id: Long): Flow<ConnectionSftp> {
        return localDataSource.get(id)
    }

    fun getAll(): Flow<List<ConnectionSftp>> {
        return localDataSource.getAll()
    }

    suspend fun insert(connection: ConnectionSftp) {
        localDataSource.insert(connection)
    }

    suspend fun update(connection: ConnectionSftp) {
        localDataSource.update(connection)
    }
}
