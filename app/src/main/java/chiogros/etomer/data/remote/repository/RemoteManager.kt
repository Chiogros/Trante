package chiogros.etomer.data.remote.repository

import android.os.Build
import androidx.annotation.RequiresApi
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
            else -> error("Connection type isn't supported yet!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun connect(con: Connection) {
        getRepositoryForObject(con).connect(con)
    }
}
