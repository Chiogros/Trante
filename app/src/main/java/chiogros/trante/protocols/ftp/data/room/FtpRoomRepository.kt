package chiogros.trante.protocols.ftp.data.room

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.RoomRepository
import kotlinx.coroutines.flow.Flow

class FtpRoomRepository(private val localDataSource: FtpRoomDataSource) :
    RoomRepository {
    override suspend fun delete(con: Connection) {
        delete(con as FtpRoom)
    }

    suspend fun delete(con: FtpRoom) {
        localDataSource.delete(con)
    }

    override fun get(id: String): Flow<FtpRoom> {
        return localDataSource.get(id)
    }

    override fun getAll(): Flow<List<FtpRoom>> {
        return localDataSource.getAll()
    }

    override suspend fun insert(con: Connection) {
        insert(con as FtpRoom)
    }

    suspend fun insert(con: FtpRoom) {
        localDataSource.insert(con)
    }

    override suspend fun update(con: Connection) {
        update(con as FtpRoom)
    }

    suspend fun update(con: FtpRoom) {
        localDataSource.update(con)
    }
}
