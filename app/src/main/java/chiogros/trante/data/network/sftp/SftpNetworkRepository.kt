package chiogros.trante.data.network.sftp

import chiogros.trante.data.network.File
import chiogros.trante.data.network.FileAttributesType
import chiogros.trante.data.network.repository.NetworkRepository
import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.data.room.sftp.SftpRoom
import org.apache.sshd.sftp.common.SftpConstants
import java.io.InputStream
import kotlin.io.path.Path

class SftpNetworkRepository(
    private val remote: RemoteSftpNetworkDataSource,
    private val local: LocalSftpNetworkDataSource
) : NetworkRepository() {
    override suspend fun connect(con: Connection): Boolean {
        if (con !is SftpRoom) {
            return false
        }

        if (local.isStillConnected(con)) {
            return true
        }

        try {
            val handler = remote.connect(
                host = con.host,
                port = 22,
                user = con.user,
                pwd = String(CryptoUtils().decrypt(con.password))
            )
            local.set(con, handler)

            return true
        } catch (_: Throwable) {
            return false
        }
    }

    override suspend fun createFile(con: Connection, path: String): Boolean {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }
        val handler: SftpNetwork = local.get(con)
        return handler.createFile(path)
    }

    override suspend fun getFileStat(con: Connection, path: String): File {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }
        val handler: SftpNetwork = local.get(con)
        val stats = handler.getFileStat(path)
        val f = File(Path(path))

        f.size = stats.size
        f.type = mapProviderTypeToGeneric(stats.type)

        return f
    }

    override suspend fun listFiles(con: Connection, path: String): List<File> {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }
        val handler: SftpNetwork = local.get(con)

        return handler.listFiles(path).map { it ->
            val f = File(Path(it.filename))
            f.type = mapProviderTypeToGeneric(it.attributes.type)
            f.size = it.attributes.size
            f
        }
    }

    fun mapProviderTypeToGeneric(type: Int): FileAttributesType =
        when (type) {
            SftpConstants.SSH_FILEXFER_TYPE_REGULAR   -> FileAttributesType.REGULAR
            SftpConstants.SSH_FILEXFER_TYPE_DIRECTORY -> FileAttributesType.DIRECTORY
            SftpConstants.SSH_FILEXFER_TYPE_SYMLINK   -> FileAttributesType.SYMLINK
            else                                      -> FileAttributesType.UNKNOWN
        }

    override suspend fun readFile(con: Connection, path: String): InputStream {
        if (con !is SftpRoom) {
            throw ClassCastException()
        }
        val handler: SftpNetwork = local.get(con)
        return handler.readFile(path)
    }
}