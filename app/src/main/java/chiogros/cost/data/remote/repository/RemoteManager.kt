package chiogros.cost.data.remote.repository

import chiogros.cost.R
import chiogros.cost.data.remote.File
import chiogros.cost.data.remote.sftp.RemoteSftpRepository
import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.sftp.ConnectionSftp
import java.io.InputStream

/**
 * Aggregates all repositories to handle data requests from SAF.
 */
class RemoteManager(private val remoteSftpRepository: RemoteSftpRepository) {
    suspend fun connect(con: Connection): Boolean {
        return getRepositoryForObject(con).connect(con)
    }

    suspend fun getFileStat(con: Connection, path: String): File {
        return getRepositoryForObject(con).getFileStat(path)
    }

    fun getRepositoryForObject(con: Connection): RemoteRepository {
        return when (con) {
            is ConnectionSftp -> remoteSftpRepository
            else -> error(R.string.connection_type_not_supported)
        }
    }

    suspend fun listFiles(con: Connection, path: String): List<File> {
        return getRepositoryForObject(con).listFiles(path)
    }

    suspend fun readFile(con: Connection, path: String): InputStream {
        return getRepositoryForObject(con).readFile(path)
    }
}
