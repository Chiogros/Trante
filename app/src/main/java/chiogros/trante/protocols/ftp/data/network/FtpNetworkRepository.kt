package chiogros.trante.protocols.ftp.data.network

import chiogros.trante.data.network.File
import chiogros.trante.data.network.FileAttributesType
import chiogros.trante.data.network.NetworkRepository
import chiogros.trante.data.room.Connection
import chiogros.trante.data.room.crypto.CryptoUtils
import chiogros.trante.protocols.ftp.data.room.FtpRoom
import org.apache.commons.net.ftp.FTPFile
import java.io.InputStream
import kotlin.io.path.Path

class FtpNetworkRepository(
    private val remote: FtpRemoteNetworkDataSource,
    private val local: FtpLocalNetworkDataSource
) : NetworkRepository {
    override suspend fun connect(con: Connection): Boolean {
        if (con !is FtpRoom) {
            return false
        }

        if (local.isStillConnected(con)) {
            return true
        }

        try {
            val handler = remote.connect(
                host = con.host,
                port = 21,
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
        if (con !is FtpRoom) {
            throw ClassCastException()
        }
        val handler: FtpNetwork = local.get(con)
        return handler.createFile(path)
    }

    override suspend fun getFileStat(con: Connection, path: String): File {
        if (con !is FtpRoom) {
            throw ClassCastException()
        }
        val handler: FtpNetwork = local.get(con)
        val stats = handler.getFileStat(path)
        val f = File(Path(path))

        f.size = stats.size
        f.type = mapProviderTypeToGeneric(stats.type)

        return f
    }

    override suspend fun listFiles(con: Connection, path: String): List<File> {
        if (con !is FtpRoom) {
            throw ClassCastException()
        }
        val handler: FtpNetwork = local.get(con)

        return handler.listFiles(path).map {
            val f = File(Path(it.name))
            f.type = mapProviderTypeToGeneric(it.type)
            f.size = it.size
            f
        }
    }

    fun mapProviderTypeToGeneric(type: Int): FileAttributesType =
        when (type) {
            FTPFile.FILE_TYPE          -> FileAttributesType.REGULAR
            FTPFile.DIRECTORY_TYPE     -> FileAttributesType.DIRECTORY
            FTPFile.SYMBOLIC_LINK_TYPE -> FileAttributesType.SYMLINK
            else                       -> FileAttributesType.UNKNOWN
        }

    override suspend fun readFile(con: Connection, path: String): InputStream {
        if (con !is FtpRoom) {
            throw ClassCastException()
        }
        val handler: FtpNetwork = local.get(con)
        return handler.readFile(path)
    }
}