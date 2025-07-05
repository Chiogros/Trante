package chiogros.etomer.data.datasource.remote

import android.os.Build
import androidx.annotation.RequiresApi
import chiogros.etomer.data.remote.RemoteSftp

class RemoteSftpDataSource(private val remote: RemoteSftp) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun connect(host: String, port: Int, user: String, pwd: String) {
        remote.connect(host, port, user, pwd)
    }
}