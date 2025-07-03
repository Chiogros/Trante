package chiogros.etomer.data.datasource

import chiogros.etomer.data.room.ConnectionSftp
import chiogros.etomer.data.room.ConnectionSftpDao
import kotlinx.coroutines.flow.Flow

class ConnectionSftpRoomDataSource(private val dao: ConnectionSftpDao) {
    suspend fun delete(connection: ConnectionSftp) {
        dao.delete(connection)
    }

    fun get(id: Long): Flow<ConnectionSftp> {
        return dao.get(id)
    }

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