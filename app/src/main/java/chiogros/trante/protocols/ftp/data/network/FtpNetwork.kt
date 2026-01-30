package chiogros.trante.protocols.ftp.data.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPConnectionClosedException
import org.apache.commons.net.ftp.FTPFile
import java.io.InputStream


class FtpNetwork {
    val coroutineDispatcher: CoroutineDispatcher
    val isConnected: Boolean
        get() = ftpClient.isConnected
    val ftpClient: FTPClient

    private constructor(coroutineDispatcher: CoroutineDispatcher, ftpClient: FTPClient) {
        this.coroutineDispatcher = coroutineDispatcher
        this.ftpClient = ftpClient
    }

    companion object {
        var coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO

        /**
         * Call new() before connect() to set dispatcher.
         */
        fun new(coroutineDispatcher: CoroutineDispatcher): Companion {
            this.coroutineDispatcher = coroutineDispatcher
            return this
        }

        suspend fun connect(host: String, port: Int, user: String, pwd: String): FtpNetwork {
            val ftp = FTPClient()

            ftp.connect(host, port)
            ftp.login(user, pwd)

            if (ftp.isConnected()) {
                return FtpNetwork(coroutineDispatcher, ftp)
            } else {
                throw Exception("Wrong login")
            }
        }
    }

    suspend fun createFile(path: String): Boolean {
        var ret: Boolean

        withContext(coroutineDispatcher) {
            try {
                ftpClient.storeFileStream(path)
                ret = true
            } catch (_: FTPConnectionClosedException) {
                ret = false
            }
        }

        return ret
    }

    suspend fun getFileStat(path: String): FTPFile =
        withContext(coroutineDispatcher) {
            ftpClient.mlistFile(path)
        }

    suspend fun listFiles(path: String): Iterable<FTPFile> =
        withContext(coroutineDispatcher) {
            ftpClient.listDirectories(path).toList()
        }

    suspend fun readFile(path: String): InputStream {
        var content: InputStream

        withContext(coroutineDispatcher) {
            content = ftpClient.retrieveFileStream(path)
        }

        return content
    }
}