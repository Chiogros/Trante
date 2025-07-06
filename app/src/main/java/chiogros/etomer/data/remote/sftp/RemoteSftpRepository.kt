package chiogros.etomer.data.remote.sftp

import chiogros.etomer.data.remote.repository.RemoteRepository
import chiogros.etomer.data.room.Connection
import chiogros.etomer.data.room.sftp.ConnectionSftp

class RemoteSftpRepository(private val remote: RemoteSftpDataSource) : RemoteRepository() {
    override suspend fun connect(con: Connection) {
        if (con is ConnectionSftp) {
            remote.connect(con.host, 22, con.user, "testtest")
        }
    }

    override suspend fun listFiles(path: String) {
        remote.listFiles(path)
    }

    override suspend fun readFile(path: String): ByteArray {
        return remote.readFile(path)
    }
}