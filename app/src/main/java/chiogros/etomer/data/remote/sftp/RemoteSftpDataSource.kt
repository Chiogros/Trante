package chiogros.etomer.data.remote.sftp

class RemoteSftpDataSource(private val remote: RemoteSftp) {
    suspend fun connect(host: String, port: Int, user: String, pwd: String) {
        remote.connect(host, port, user, pwd)
    }
}