package chiogros.trante.protocols.sftp.data.room

import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.RoomRepository
import kotlinx.coroutines.flow.Flow

class SftpRoomRepository(private val localDataSource: SftpRoomDataSource) :
    RoomRepository() {
    override suspend fun delete(con: Connection) {
        delete(con as SftpRoom)
    }

    suspend fun delete(con: SftpRoom) {
        localDataSource.delete(con)
    }

    override fun get(id: String): Flow<SftpRoom> {
        return localDataSource.get(id)
    }

    override fun getAll(): Flow<List<SftpRoom>> {
        return localDataSource.getAll()
    }

    override suspend fun insert(con: Connection) {
        insert(con as SftpRoom)
    }

    suspend fun insert(con: SftpRoom) {
        localDataSource.insert(con)
    }

    override suspend fun update(con: Connection) {
        update(con as SftpRoom)
    }

    suspend fun update(con: SftpRoom) {
        localDataSource.update(con)
    }
}
