package chiogros.cost.data.network.repository

import chiogros.cost.R
import chiogros.cost.data.network.File
import chiogros.cost.data.network.sftp.SftpNetworkRepository
import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.sftp.SftpRoom
import java.io.InputStream

/**
 * Aggregates all repositories to handle data requests from SAF.
 */
class NetworkManager(private val sftpNetworkRepository: SftpNetworkRepository) {
    suspend fun createFile(con: Connection, path: String): Boolean {
        return getRepositoryForObject(con).createFile(con, path)
    }

    suspend fun connect(con: Connection): Boolean {
        return getRepositoryForObject(con).connect(con)
    }

    suspend fun getFileStat(con: Connection, path: String): File {
        return getRepositoryForObject(con).getFileStat(con, path)
    }

    fun getRepositoryForObject(con: Connection): NetworkRepository {
        return when (con) {
            is SftpRoom -> sftpNetworkRepository
            else        -> error(R.string.connection_type_not_supported)
        }
    }

    suspend fun listFiles(con: Connection, path: String): List<File> {
        return getRepositoryForObject(con).listFiles(con, path)
    }

    suspend fun readFile(con: Connection, path: String): InputStream {
        return getRepositoryForObject(con).readFile(con, path)
    }
}
