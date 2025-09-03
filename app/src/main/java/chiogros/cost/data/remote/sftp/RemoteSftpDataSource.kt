package chiogros.cost.data.remote.sftp

class RemoteSftpDataSource(private val remote: RemoteSftp.Companion) {
    suspend fun connect(host: String, port: Int, user: String, pwd: String): RemoteSftp {
        return remote.connect(host, port, user, pwd)
    }
}