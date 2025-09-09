package chiogros.trante.data.room.sftp

import kotlinx.coroutines.flow.Flow

class SftpRoomDataSource(private val dao: SftpRoomDao) {
    suspend fun delete(con: SftpRoom) {
        dao.delete(con)
    }

    fun get(id: String): Flow<SftpRoom> {
        return dao.get(id)
    }

    fun getAll(): Flow<List<SftpRoom>> {
        return dao.getAll()
    }

    suspend fun insert(con: SftpRoom) {
        dao.insert(con)
    }

    suspend fun update(con: SftpRoom) {
        dao.update(con)
    }

}