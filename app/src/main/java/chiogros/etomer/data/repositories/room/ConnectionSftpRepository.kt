package chiogros.etomer.data.repositories.room

import chiogros.etomer.data.datasource.room.ConnectionSftpRoomDataSource
import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.ConnectionSftp
import kotlinx.coroutines.flow.Flow

class ConnectionSftpRepository(private val localDataSource: ConnectionSftpRoomDataSource) :
    ConnectionRepository() {
    override suspend fun delete(con: Connection) {
        delete(con as ConnectionSftp)
    }

    suspend fun delete(con: ConnectionSftp) {
        localDataSource.delete(con)
    }

    override fun get(id: String): Flow<ConnectionSftp> {
        return localDataSource.get(id)
    }

    override fun getAll(): Flow<List<ConnectionSftp>> {
        return localDataSource.getAll()
    }

    override suspend fun insert(con: Connection) {
        insert(con as ConnectionSftp)
    }

    suspend fun insert(con: ConnectionSftp) {
        localDataSource.insert(con)
    }

    override suspend fun update(con: Connection) {
        update(con as ConnectionSftp)
    }

    suspend fun update(con: ConnectionSftp) {
        localDataSource.update(con)
    }
}
