package chiogros.etomer.data.repositories

import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.ConnectionSftp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

/**
 * Aggregates all repositories to handle data requests from UI.
 */
class ConnectionManager(private val connectionSftpRepository: ConnectionSftpRepository) {
    fun getRepositoryForObject(con: Connection): ConnectionRepository {
        return when (con) {
            is ConnectionSftp -> connectionSftpRepository
            else -> error("Connection type isn't supported yet!")
        }
    }

    suspend fun delete(con: Connection) {
        getRepositoryForObject(con).delete(con)
    }

    fun get(id: String): Flow<Connection> {
        return connectionSftpRepository.get(id)
    }

    fun getAll(): Flow<List<Connection>> {
        return merge(connectionSftpRepository.getAll())
    }

    suspend fun insert(con: Connection) {
        getRepositoryForObject(con).insert(con)
    }

    suspend fun update(con: Connection) {
        getRepositoryForObject(con).update(con)
    }
}
