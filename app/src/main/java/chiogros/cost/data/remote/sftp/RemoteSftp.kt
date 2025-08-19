package chiogros.cost.data.remote.sftp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.session.ClientSession
import org.apache.sshd.common.util.io.PathUtils.setUserHomeFolderResolver
import org.apache.sshd.sftp.client.SftpClient
import org.apache.sshd.sftp.client.SftpClientFactory
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Supplier

class RemoteSftp(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    val client: SshClient
    lateinit var sftpClient: SftpClient

    init {
        // Set Android's filesystem path
        val path: Supplier<Path> = Supplier { Paths.get("") }
        setUserHomeFolderResolver(path)

        client = SshClient.setUpDefaultClient()
        client.start()
    }

    suspend fun connect(host: String, port: Int, user: String, pwd: String): Boolean {
        var session: ClientSession? = null

        withContext(coroutineDispatcher) {
            try {
                // Connect to server
                session = client.connect(user, host, port)
                    .verify()
                    .clientSession

                session.addPasswordIdentity(pwd)

                if (session.auth().verify().isSuccess) {
                    val factory: SftpClientFactory = SftpClientFactory.instance()
                    sftpClient = factory.createSftpClient(session)
                } else {
                    session = null
                }

            } catch (_: IOException) {
                session = null
            }
        }

        return (session != null)
    }

    suspend fun listFiles(path: String): Iterable<SftpClient.DirEntry> =
        withContext(coroutineDispatcher) {
            sftpClient.readEntries(sftpClient.canonicalPath(path))
        }

    fun readFile(path: String): ByteArray {
        var content: ByteArray

        try {
            val canonicalPath = sftpClient.canonicalPath(path)
            val fileSize: Long = sftpClient.stat(canonicalPath).size
            sftpClient.open(canonicalPath)

            // ByteArray max size is integer max value. So at this time, truncate larger files
            val bufSize: Int = if (fileSize <= Int.MAX_VALUE) fileSize.toInt() else Int.MAX_VALUE
            content = ByteArray(bufSize)

            sftpClient.read(canonicalPath).read(content)

        } catch (_: IOException) {
            content = ByteArray(0)
        }

        return content
    }
}