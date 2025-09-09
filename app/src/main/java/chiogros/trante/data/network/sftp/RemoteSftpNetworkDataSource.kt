package chiogros.trante.data.network.sftp

class RemoteSftpNetworkDataSource(private val remote: SftpNetwork.Companion) {
    suspend fun connect(host: String, port: Int, user: String, pwd: String): SftpNetwork {
        return remote.connect(host, port, user, pwd)
    }
}