package chiogros.etomer.data.remote.sftp

import android.os.Build
import androidx.annotation.RequiresApi
import chiogros.etomer.data.remote.repository.RemoteRepository
import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.sftp.ConnectionSftp

class RemoteSftpRepository(private val remote: RemoteSftpDataSource) : RemoteRepository() {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun connect(con: Connection) {
        if (con is ConnectionSftp) {
            remote.connect(con.host, 22, con.user, "testtest")
        }
    }
}