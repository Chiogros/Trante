package chiogros.trante.data.network.sftp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.session.ClientSession
import org.apache.sshd.common.util.buffer.Buffer
import org.apache.sshd.common.util.io.PathUtils.setUserHomeFolderResolver
import org.apache.sshd.sftp.client.SftpClient
import org.apache.sshd.sftp.client.SftpVersionSelector
import org.apache.sshd.sftp.client.impl.DefaultSftpClient
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Supplier


class SftpNetwork {
    val coroutineDispatcher: CoroutineDispatcher
    val isConnected: Boolean
        get() = sftpClient.isOpen
    val sftpClient: SftpClient

    private constructor(coroutineDispatcher: CoroutineDispatcher, sftpClient: SftpClient) {
        this.coroutineDispatcher = coroutineDispatcher
        this.sftpClient = sftpClient
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

        suspend fun connect(host: String, port: Int, user: String, pwd: String): SftpNetwork {
            // Set Android's filesystem path
            val path: Supplier<Path> = Supplier { Paths.get("") }
            setUserHomeFolderResolver(path)
            val client: SshClient = SshClient.setUpDefaultClient()

            client.start()

            return withContext(coroutineDispatcher) {
                // Connect to server
                val session = client.connect(user, host, port).verify().clientSession
                session.addPasswordIdentity(pwd)

                val authVerif = session.auth()

                // TODO: verify() doesn't work, timeout skips time
                // and reaches time limit
                while (!authVerif.isDone) {
                }

                if (authVerif.isSuccess) SftpNetwork(
                    coroutineDispatcher,
                    ConcurrentSftpClient(session)
                )
                else throw authVerif.exception
            }
        }
    }

    suspend fun createFile(path: String): Boolean {
        var ret: Boolean

        withContext(coroutineDispatcher) {
            try {
                sftpClient.write(path, SftpClient.OpenMode.Create)
                ret = true
            } catch (_: IOException) {
                ret = false
            }
        }

        return ret
    }

    suspend fun getFileStat(path: String): SftpClient.Attributes =
        withContext(coroutineDispatcher) {
            sftpClient.stat(sftpClient.canonicalPath(path))
        }

    suspend fun listFiles(path: String): Iterable<SftpClient.DirEntry> =
        withContext(coroutineDispatcher) {
            sftpClient.readEntries(sftpClient.canonicalPath(path))
        }

    suspend fun readFile(path: String): InputStream {
        var content: InputStream

        withContext(coroutineDispatcher) {
            val canonicalPath: String = sftpClient.canonicalPath(path)
            sftpClient.open(canonicalPath)

            content = sftpClient.read(canonicalPath)
        }

        return content
    }

    class ConcurrentSftpClient internal constructor(clientSession: ClientSession) :
        DefaultSftpClient(clientSession, SftpVersionSelector.CURRENT, EMPTY) {
        private val sendLock: Lock = ReentrantLock()

        override fun send(cmd: Int, buffer: Buffer?): Int {
            this.sendLock.lock()
            try {
                return super.send(cmd, buffer)
            } finally {
                this.sendLock.unlock()
            }
        }
    }
}