package chiogros.etomer.data.remote.sftp

import org.apache.sshd.sftp.client.SftpClient

class RemoteSftpDataSource(private val remote: RemoteSftp) {
    suspend fun connect(host: String, port: Int, user: String, pwd: String): Boolean {
        return remote.connect(host, port, user, pwd)
    }

    suspend fun listFiles(path: String): Iterable<SftpClient.DirEntry> {
        return remote.listFiles(path)
    }

    suspend fun readFile(path: String): ByteArray {
        return remote.readFile(path)
    }
}