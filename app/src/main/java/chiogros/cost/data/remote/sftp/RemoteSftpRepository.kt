package chiogros.cost.data.remote.sftp

import chiogros.cost.data.remote.File
import chiogros.cost.data.remote.FileAttributesType
import chiogros.cost.data.remote.repository.RemoteRepository
import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.sftp.ConnectionSftp
import org.apache.sshd.sftp.common.SftpConstants
import kotlin.io.path.Path

class RemoteSftpRepository(private val remote: RemoteSftpDataSource) : RemoteRepository() {
    override suspend fun connect(con: Connection): Boolean {
        if (con is ConnectionSftp) {
            return remote.connect(con.host, 22, con.user, con.password)
        }
        return false
    }

    override suspend fun listFiles(path: String): List<File> {
        return remote.listFiles(path).map { it ->
            val f = File(Path(it.filename))
            f.type = when (it.attributes.type) {
                SftpConstants.SSH_FILEXFER_TYPE_REGULAR -> FileAttributesType.REGULAR
                SftpConstants.SSH_FILEXFER_TYPE_DIRECTORY -> FileAttributesType.DIRECTORY
                SftpConstants.SSH_FILEXFER_TYPE_SYMLINK -> FileAttributesType.SYMLINK
                else -> {
                    FileAttributesType.UNKNOWN
                }
            }
            f.size = it.attributes.size
            f
        }
    }

    override suspend fun readFile(path: String): ByteArray {
        return remote.readFile(path)
    }
}