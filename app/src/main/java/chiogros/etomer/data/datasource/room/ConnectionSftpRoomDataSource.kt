package chiogros.etomer.data.datasource.room

import chiogros.etomer.data.room.ConnectionSftp
import chiogros.etomer.data.room.ConnectionSftpDao
import kotlinx.coroutines.flow.Flow

class ConnectionSftpRoomDataSource(private val dao: ConnectionSftpDao) {
    suspend fun delete(con: ConnectionSftp) {
        dao.delete(con)
    }

    fun get(id: String): Flow<ConnectionSftp> {
        return dao.get(id)
    }

    fun getAll(): Flow<List<ConnectionSftp>> {
        return dao.getAll()
    }

    suspend fun insert(con: ConnectionSftp) {
        dao.insert(con)
    }

    suspend fun update(con: ConnectionSftp) {
        dao.update(con)
    }

}