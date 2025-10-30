package chiogros.trante.data.room.repository

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.sftp.data.room.SftpRoom
import chiogros.trante.protocols.sftp.data.room.SftpRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

/**
 * Aggregates all repositories to handle data requests from UI.
 */
class RoomManager(private val sftpRoomRepository: SftpRoomRepository) {
    suspend fun delete(con: Connection) {
        getRepositoryForObject(con).delete(con)
    }

    fun get(id: String): Flow<Connection> {
        return sftpRoomRepository.get(id)
    }

    fun getAll(): Flow<List<Connection>> {
        return merge(sftpRoomRepository.getAll())
    }

    fun getRepositoryForObject(con: Connection): RoomRepository {
        return when (con) {
            is SftpRoom -> sftpRoomRepository
            else        -> error("Connection type isn't supported yet!")
        }
    }

    suspend fun insert(con: Connection) {
        getRepositoryForObject(con).insert(con)
    }

    suspend fun update(con: Connection) {
        getRepositoryForObject(con).update(con)
    }
}
