package chiogros.etomer.data.remote.sftp

import android.os.Build
import androidx.annotation.RequiresApi

class RemoteSftpDataSource(private val remote: RemoteSftp) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun connect(host: String, port: Int, user: String, pwd: String) {
        remote.connect(host, port, user, pwd)
    }
}