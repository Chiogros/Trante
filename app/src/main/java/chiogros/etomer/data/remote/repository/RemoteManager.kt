package chiogros.etomer.data.remote.repository

import chiogros.etomer.R
import chiogros.etomer.data.remote.sftp.RemoteSftpRepository
import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.sftp.ConnectionSftp

/**
 * Aggregates all repositories to handle data requests from SAF.
 */
class RemoteManager(private val remoteSftpRepository: RemoteSftpRepository) {
    fun getRepositoryForObject(con: Connection): RemoteRepository {
        return when (con) {
            is ConnectionSftp -> remoteSftpRepository
            else -> error(R.string.connection_type_not_supported)
        }
    }

    suspend fun connect(con: Connection): Boolean {
        return getRepositoryForObject(con).connect(con)
    }

    suspend fun listFiles(path: String) {
        //getRepositoryForObject(con).listFiles(path)
    }

    suspend fun readFile(path: String): ByteArray {
        //getRepositoryForObject(con).readFile(path)
        return ByteArray(0)
    }
}
