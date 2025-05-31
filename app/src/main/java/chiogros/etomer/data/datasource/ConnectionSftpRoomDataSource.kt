package chiogros.etomer.data.datasource

import chiogros.etomer.data.storage.ConnectionSftp
import chiogros.etomer.data.storage.ConnectionSftpDao
import kotlinx.coroutines.flow.Flow

class ConnectionSftpRoomDataSource(private val dao: ConnectionSftpDao) {
    fun getAll(): Flow<List<ConnectionSftp>> {
        return dao.getAll()
    }

    suspend fun insert(connection: ConnectionSftp) {
        dao.insert(connection)
    }

    suspend fun update(connection: ConnectionSftp) {
        dao.update(connection)
    }
}