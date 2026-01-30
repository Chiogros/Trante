package chiogros.trante.protocols.ftp.data.network

class FtpRemoteNetworkDataSource(private val remote: FtpNetwork.Companion) {
    suspend fun connect(host: String, port: Int, user: String, pwd: String): FtpNetwork {
        return remote.connect(host, port, user, pwd)
    }
}