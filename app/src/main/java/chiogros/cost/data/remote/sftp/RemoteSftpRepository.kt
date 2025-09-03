package chiogros.cost.data.remote.sftp

import chiogros.cost.data.remote.File
import chiogros.cost.data.remote.FileAttributesType
import chiogros.cost.data.remote.repository.RemoteRepository
import chiogros.cost.data.room.Connection
import chiogros.cost.data.room.sftp.ConnectionSftp
import org.apache.sshd.sftp.common.SftpConstants
import java.io.InputStream
import kotlin.io.path.Path

class RemoteSftpRepository(
    private val remote: RemoteSftpDataSource,
    private val cache: LocalSftpDataSource
) : RemoteRepository() {
    override suspend fun connect(con: Connection): Boolean {
        if (con !is ConnectionSftp) {
            return false
        }

        if (cache.isStillConnected(con)) {
            return true
        }

        try {
            val handler = remote.connect(con.host, 22, con.user, con.password)
            cache.set(con, handler)

            return true
        } catch (_: Throwable) {
            return false
        }
    }

    override suspend fun getFileStat(con: Connection, path: String): File {
        if (con !is ConnectionSftp) {
            throw ClassCastException()
        }

        val handler: RemoteSftp = cache.get(con)

        val stats = handler.getFileStat(path)
        val f = File(Path(path))

        f.size = stats.size
        f.type = mapProviderTypeToGeneric(stats.type)

        return f
    }

    override suspend fun listFiles(con: Connection, path: String): List<File> {
        if (con !is ConnectionSftp) {
            throw ClassCastException()
        }

        val handler: RemoteSftp = cache.get(con)

        return handler.listFiles(path).map { it ->
            val f = File(Path(it.filename))
            f.type = mapProviderTypeToGeneric(it.attributes.type)
            f.size = it.attributes.size
            f
        }
    }

    fun mapProviderTypeToGeneric(type: Int): FileAttributesType =
        when (type) {
            SftpConstants.SSH_FILEXFER_TYPE_REGULAR -> FileAttributesType.REGULAR
            SftpConstants.SSH_FILEXFER_TYPE_DIRECTORY -> FileAttributesType.DIRECTORY
            SftpConstants.SSH_FILEXFER_TYPE_SYMLINK -> FileAttributesType.SYMLINK
            else -> {
                FileAttributesType.UNKNOWN
            }
        }

    override suspend fun readFile(con: Connection, path: String): InputStream {
        if (con !is ConnectionSftp) {
            throw ClassCastException()
        }

        val handler: RemoteSftp = cache.get(con)
        return handler.readFile(path)
    }
}