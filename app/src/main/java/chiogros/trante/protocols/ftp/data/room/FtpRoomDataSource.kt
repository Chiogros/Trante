package chiogros.trante.protocols.ftp.data.room

import kotlinx.coroutines.flow.Flow

class FtpRoomDataSource(private val dao: FtpRoomDao) {
    suspend fun delete(con: FtpRoom) {
        dao.delete(con)
    }

    fun get(id: String): Flow<FtpRoom> {
        return dao.get(id)
    }

    fun getAll(): Flow<List<FtpRoom>> {
        return dao.getAll()
    }

    suspend fun insert(con: FtpRoom) {
        dao.insert(con)
    }

    suspend fun update(con: FtpRoom) {
        dao.update(con)
    }

}